package net.fabricmc.scrap.block.entity.ducts.energyducts;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EnergyNetwork extends SnapshotParticipant<Long> implements EnergyStorage {
    private final long maxInsert = 100000;
    private final long maxExtract = 100000;
    private final int id;
    private final List<ConductiveEnergyDuctBlockEntity> cables = new LinkedList<>();
    private long capacity = 0;
    private long amount = 0;

    public EnergyNetwork() {
        Random idGen = new Random();
        this.id = idGen.nextInt();
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return cables.size();
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

    protected Long createSnapshot() {
        return amount;
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        amount = snapshot;
    }

    @Override
    public boolean supportsInsertion() {
        return true;
    }

    @Override
    public boolean supportsExtraction() {
        return true;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    //This is broken.
    public void merge(EnergyNetwork network) {
        capacity += network.capacity;
        amount += network.amount;
        // merge lists
        for (BlockEntity cable : network.cables) {
            ConductiveEnergyDuctBlockEntity castCable = (ConductiveEnergyDuctBlockEntity) cable;
            cables.add(castCable);
            castCable.setEnergyNetwork(this);
            castCable.markDirty();
        }
        System.out.println("Merged network");
    }

    public void join(ConductiveEnergyDuctBlockEntity entity) {
        capacity += ConductiveEnergyDuctBlockEntity.capacity;
        cables.add(entity);
    }

    //Works
    public void quit(BlockPos pos) {
        if (!cables.isEmpty()) {
            ConductiveEnergyDuctBlockEntity toRemove=null;
            for (ConductiveEnergyDuctBlockEntity cable : cables) {
                if (cable.getPos().equals(pos)) {
                    toRemove = cable;

                }
            }
            if (toRemove != null) {
                cables.remove(toRemove);
                capacity -= ConductiveEnergyDuctBlockEntity.capacity;
                if (cables.size() > 0) {
                    amount -= amount / cables.size();
                }
            }

        }
    }

    private LinkedList<ConductiveEnergyDuctBlockEntity> findAllNeighbors(ConductiveEnergyDuctBlockEntity firstBlock, World world) {
        LinkedList<ConductiveEnergyDuctBlockEntity> adjacentBlocks = new LinkedList<ConductiveEnergyDuctBlockEntity>();
        adjacentBlocks.add(firstBlock);

        boolean completed = false;
        while (!completed) {
            //For each block
            //For every Block search for entitys
            LinkedList<ConductiveEnergyDuctBlockEntity> newBlocks = new LinkedList<ConductiveEnergyDuctBlockEntity>();
            for (ConductiveEnergyDuctBlockEntity block : adjacentBlocks) {
                for (Direction direction : Direction.values()) {
                    BlockEntity neighbour = world.getBlockEntity(block.getPos().offset(direction));
                    if (neighbour != null && neighbour.getType() == block.getType()) {
                        newBlocks.add((ConductiveEnergyDuctBlockEntity) neighbour);
                    }
                }
            }//For each block
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

    //This is broken the new network of the ones that have been split their blocks are random networks .
    public void split(BlockPos pos, World world) {
        quit(pos);

        //Split here
        List<ConductiveEnergyDuctBlockEntity> existingCables = this.cables;
        List<List<ConductiveEnergyDuctBlockEntity>> networks = new LinkedList<>();
        while (!existingCables.isEmpty()) {
            List<ConductiveEnergyDuctBlockEntity> network = new LinkedList<>();
            ConductiveEnergyDuctBlockEntity firstBlock = existingCables.get(0);
            existingCables.remove(firstBlock);
            network = findAllNeighbors(firstBlock, world);
            for (ConductiveEnergyDuctBlockEntity block : network) {
                existingCables.remove(block);
            }
            networks.add(network);
        }


        //Do the split
        if (networks.size() == 1) {
            return;
        } else if (networks.size() > 1) {
            //For Each network
            System.out.println("SPLITTED INTO " + networks.size() + " NETWORKS");
            for (int i = 0; i < networks.size(); i++) {
                EnergyNetwork network = new EnergyNetwork();
                //For each cable of network
                for (int n = 0; n < networks.get(i).size(); n++) {
                    //set the network to cable
                    networks.get(i).get(n).setEnergyNetwork(network);
                    //join the network
                    network.join(networks.get(i).get(n));
                }
                //TODO Set new amount for both network and capacity
            }
        }
    }
}
