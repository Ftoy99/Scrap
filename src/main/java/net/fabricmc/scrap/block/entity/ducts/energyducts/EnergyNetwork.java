package net.fabricmc.scrap.block.entity.ducts.energyducts;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EnergyNetwork extends SnapshotParticipant<Long> implements EnergyStorage {
    public long amount = 0;
    public long capacity = 0;
    public long maxInsert = Long.MAX_VALUE;
    public long maxExtract = Long.MAX_VALUE;
    public ArrayList<ConductiveEnergyDuctBlockEntity> cables = new ArrayList<>();
    public BlockPos master;

    public EnergyNetwork(ConductiveEnergyDuctBlockEntity cable) {
        master = cable.getPos();
        cables.add(cable);
        capacity += cable.getCapacity();
    }

    public BlockPos getMaster() {
        return master;
    }

    public void setMaster(BlockPos master) {
        this.master = master;
    }

    @Override
    public boolean supportsInsertion() {
        return amount < capacity;
    }

    @Override
    protected Long createSnapshot() {
        return amount;
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        amount = snapshot;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);

        long inserted = Math.min(maxInsert, Math.min(maxAmount, capacity - amount));

        if (inserted > 0) {
            updateSnapshots(transaction);
            amount += inserted;
            return inserted;
        }

        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return amount > 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);

        long extracted = Math.min(maxExtract, Math.min(maxAmount, amount));

        if (extracted > 0) {
            updateSnapshots(transaction);
            amount -= extracted;
            return extracted;
        }

        return 0;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public void join(ConductiveEnergyDuctBlockEntity cable) {
        cables.add(cable);
        capacity += cable.getCapacity();
    }

    public void merge(EnergyNetwork netToMerge) {
        System.out.println("Merging");
        for (ConductiveEnergyDuctBlockEntity cableToMerge : netToMerge.cables) {
            cableToMerge.setMasterPos(master);
            cableToMerge.setEnergyNetwork(this);
            cableToMerge.setMaster(false);
        }
        cables.addAll(netToMerge.cables);
        capacity += netToMerge.getCapacity();
        amount += netToMerge.getAmount();
    }

    private LinkedList<ConductiveEnergyDuctBlockEntity> findAllNeighbors(ConductiveEnergyDuctBlockEntity firstBlock, World world, BlockPos cable) {
        LinkedList<ConductiveEnergyDuctBlockEntity> adjacentBlocks = new LinkedList<>();
        adjacentBlocks.add(firstBlock);
        boolean completed = false;
        while (!completed) {
            //For every Block search for entitys
            LinkedList<ConductiveEnergyDuctBlockEntity> newBlocks = new LinkedList<>();
            for (ConductiveEnergyDuctBlockEntity block : adjacentBlocks) {
                for (Direction direction : Direction.values()) {
                    BlockEntity neighbour = world.getBlockEntity(block.getPos().offset(direction));
                    if (neighbour != null && neighbour.getType() == block.getType() && !newBlocks.contains(neighbour) && !neighbour.getPos().equals(cable)) {
                        newBlocks.add((ConductiveEnergyDuctBlockEntity) neighbour);
                    }
                }
            }//For each block
            //If new Blocks where found from previous iteration redo else return.
            completed = true;
            for (ConductiveEnergyDuctBlockEntity block : newBlocks) {
                if (!adjacentBlocks.contains(block)) {
                    adjacentBlocks.add(block);
                    completed = false;
                }
            }
        }
        return adjacentBlocks;
    }

    public void split(World world, BlockPos cable) {
        quit(cable);
        List<ConductiveEnergyDuctBlockEntity> existingCables = cables;
        List<List<ConductiveEnergyDuctBlockEntity>> networks = new LinkedList<>();
        while (!existingCables.isEmpty()) {
            ConductiveEnergyDuctBlockEntity firstBlock = existingCables.get(0);
            List<ConductiveEnergyDuctBlockEntity> network = findAllNeighbors(firstBlock, world, cable);
            for (ConductiveEnergyDuctBlockEntity block : network) {
                existingCables.remove(block);
            }
            networks.add(network);
        }
        if (networks.size() == 1) {
            List<ConductiveEnergyDuctBlockEntity> net = networks.get(0);
            ConductiveEnergyDuctBlockEntity master = net.get(0);
            System.out.println("Selected master:"+master);
            master.setMaster(true);
            EnergyNetwork newNet = new EnergyNetwork(master);
            for (ConductiveEnergyDuctBlockEntity netCable : net) {
                System.out.println("Setting master");
                netCable.setEnergyNetwork(newNet);
                netCable.setMasterPos(master.getPos());
                newNet.join(netCable);
            }
            //set power
            if (cables.size() != 0) {
                newNet.amount = (newNet.cables.size() / cables.size()) * amount;
            }
        } else if (networks.size() > 1) {
            for (List<ConductiveEnergyDuctBlockEntity> net : networks) {
                EnergyNetwork newNet = new EnergyNetwork(net.get(0));
                net.get(0).setMaster(true);
                for (ConductiveEnergyDuctBlockEntity netCable : net) {
                    netCable.setEnergyNetwork(newNet);
                    netCable.setMasterPos(net.get(0).getPos());
                    newNet.join(netCable);
                }
                //set power
                if (cables.size() != 0) {
                    newNet.amount = (newNet.cables.size() / cables.size()) * amount;
                }
            }
        }
    }

    public void quit(BlockPos cable) {
        ConductiveEnergyDuctBlockEntity cableToRemove = null;
        for (ConductiveEnergyDuctBlockEntity maybeCableToRemove : cables) {
            if (maybeCableToRemove.getPos().equals(cable)) {
                cableToRemove = maybeCableToRemove;
            }
        }
        if (cableToRemove != null) {
            boolean quited = cables.remove(cableToRemove);
        }
    }

}
