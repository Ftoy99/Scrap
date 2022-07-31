package net.fabricmc.scrap.block.entity.ducts.energyducts;

import net.fabricmc.scrap.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class ConductiveEnergyDuctBlockEntity extends BlockEntity {
    public static int maxExtract = 64;
    public static int maxInsert = 64;
    public int capacity = 512;
    public EnergyNetwork energyNetwork;
    public BlockPos masterPos;
    public boolean master;

    public ConductiveEnergyDuctBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONDUCTIVE_ENERGY_DUCT_BLOCK_ENTITY, pos, state);
        energyNetwork = null;
        master = false;
        masterPos = null;
    }


    public static void tick(World world, BlockPos pos, BlockState state, ConductiveEnergyDuctBlockEntity entity) {
        checkNetwork(world, entity);
        if (entity.getEnergyNetwork() != null) {
            List<EnergyStorage> extractFrom = new ArrayList<>();
            List<EnergyStorage> insertTo = new ArrayList<>();

            //Initialize
            for (Direction direction : Direction.values()) {
                EnergyStorage maybeStorage = SimpleEnergyStorage.SIDED.find(world, pos.offset(direction, 1), direction);
                if (maybeStorage != null) {
                    if (maybeStorage.supportsExtraction()) {
                        extractFrom.add(maybeStorage);
                    } else if (maybeStorage.supportsInsertion()) {
                        if (maybeStorage.supportsInsertion()) {
                            insertTo.add(maybeStorage);
                        }
                    }
                }
            }
            //Extract Energy
            for (EnergyStorage extractTarget : extractFrom) {
                EnergyStorageUtil.move(
                        extractTarget, // from source
                        entity.getEnergyNetwork(), // into target
                        maxExtract,
                        null // create a new transaction for this operation
                );
            }
            //Insert Energy
            for (EnergyStorage insertTarget : insertTo) {
                EnergyStorageUtil.move(
                        entity.getEnergyNetwork(), // from source
                        insertTarget, // into target
                        maxInsert,
                        null // create a new transaction for this operation
                );
            }

        }

    }

    private static void checkNetwork(World world, ConductiveEnergyDuctBlockEntity entity) {
        if (entity.masterPos != null && entity.energyNetwork == null) {
            BlockEntity maybeCable = world.getBlockEntity(entity.masterPos);
            if (maybeCable != null && maybeCable.getType() == ModBlockEntities.CONDUCTIVE_ENERGY_DUCT_BLOCK_ENTITY) {
                ConductiveEnergyDuctBlockEntity cable = (ConductiveEnergyDuctBlockEntity) maybeCable;
                if (cable.getEnergyNetwork() != null) {
                    entity.setEnergyNetwork(cable.getEnergyNetwork());
                }
            }
        }
    }

    public int getMaxExtract() {
        return maxExtract;
    }

    public int getMaxInsert() {
        return maxInsert;
    }

    public int getCapacity() {
        return capacity;
    }

    public EnergyNetwork getEnergyNetwork() {
        return energyNetwork;
    }

    public void setEnergyNetwork(EnergyNetwork energyNetwork) {
        this.energyNetwork = energyNetwork;
    }

    public BlockPos getMasterPos() {
        return masterPos;
    }

    public void setMasterPos(BlockPos masterPos) {
        this.masterPos = masterPos;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public void initializeNetwork() {
        if (!this.getWorld().isClient) {
            List<ConductiveEnergyDuctBlockEntity> neighbours = this.getNeighbours();
            List<EnergyNetwork> neighbourNets = new ArrayList<>();
            for (ConductiveEnergyDuctBlockEntity neighbour:neighbours){
                if(neighbour.getEnergyNetwork()!=null && !neighbourNets.contains(neighbour.getEnergyNetwork())){
                    neighbourNets.add(neighbour.getEnergyNetwork());
                }
            }
            System.out.println("Size"+neighbours.size());
            if (neighbourNets.isEmpty()) {
                //create new net
                EnergyNetwork net = new EnergyNetwork(this);
                energyNetwork = net;
                master = true;
            } else if (neighbourNets.size() == 1) {
                //join network
                EnergyNetwork neighbour = neighbourNets.get(0);
                energyNetwork = neighbour;
                masterPos = energyNetwork.getMaster();
                energyNetwork.join(this);
            } else if (neighbourNets.size() > 1) {
                List<EnergyNetwork> nets = new ArrayList<>();
                EnergyNetwork net = new EnergyNetwork(this);
                energyNetwork = net;
                master = true;
                for (EnergyNetwork netToMerge:neighbourNets){
                    energyNetwork.merge(netToMerge);
                }
            }
        }
    }

    public List<ConductiveEnergyDuctBlockEntity> getNeighbours() {
        List<ConductiveEnergyDuctBlockEntity> neighbours = new ArrayList<>();
        if (this.getWorld() != null && !this.getWorld().isClient) {
            for (Direction direction : Direction.values()) {
                BlockEntity maybeCable = world.getBlockEntity(this.getPos().offset(direction));
                if (maybeCable != null && maybeCable.getType() == this.getType()) {
                    neighbours.add((ConductiveEnergyDuctBlockEntity) maybeCable);
                }
            }
        }
        return neighbours;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("cable.master", master);
        if (master == true) {
            nbt.putLong("cable.amount", energyNetwork.getAmount());
        } else {
            nbt.putInt("cable.masterX", masterPos.getX());
            nbt.putInt("cable.masterY", masterPos.getY());
            nbt.putInt("cable.masterZ", masterPos.getZ());
        }

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        master = nbt.getBoolean("cable.master");
        if (master == true) {
            energyNetwork = new EnergyNetwork(this);
            energyNetwork.setAmount(nbt.getLong("cable.amount"));
        } else {
            masterPos = new BlockPos(nbt.getInt("cable.masterX"), nbt.getInt("cable.masterY"), nbt.getInt("cable.masterZ"));
        }
    }
}
