package net.fabricmc.scrap.block.entity.ducts.energyducts;

import net.fabricmc.scrap.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;
import java.util.List;


public class ConductiveEnergyDuctBlockEntity extends BlockEntity {
    public static final int maxExtract = 64;
    public static final int maxInsert = 64;
    public static final int capacity = 512;
    private EnergyNetwork energyNetwork=null;

    public ConductiveEnergyDuctBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONDUCTIVE_ENERGY_DUCT_BLOCK_ENTITY, pos, state);
    }


    private static void initNetwork(World world, BlockPos pos, ConductiveEnergyDuctBlockEntity entity) {
        //Get all neighboors
        if (world.isClient) {
            return;
        }
        List<ConductiveEnergyDuctBlockEntity> neighboors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockEntity neighboor = world.getBlockEntity(pos.offset(direction, 1));
            if (neighboor != null) {
                if (neighboor.getType() == world.getBlockEntity(pos).getType()) {
                    neighboors.add((ConductiveEnergyDuctBlockEntity) neighboor);
                }
            }

        }
        if (neighboors.size() == 0) {
            //Create network
            entity.energyNetwork = new EnergyNetwork();
            entity.energyNetwork.join(entity);
            return;
        } else if (neighboors.size() == 1) {
            //Join adjacent Networks
            entity.setEnergyNetwork(neighboors.get(0).getEnergyNetwork());
            neighboors.get(0).getEnergyNetwork().join(entity);
            System.out.println("Joined Network" + entity.energyNetwork);
        } else {
            //Merge networks
            List<EnergyNetwork> neighbourNetwork = new ArrayList<>();
            for (ConductiveEnergyDuctBlockEntity neighbour : neighboors) {
                if (neighbour.getEnergyNetwork() != null && !neighbourNetwork.contains(neighbour.getEnergyNetwork()))
                    neighbourNetwork.add(neighbour.getEnergyNetwork());
            }
            EnergyNetwork finalNet =neighbourNetwork.get(0);
            neighbourNetwork.remove(0);
            for (EnergyNetwork network : neighbourNetwork) {
                System.out.println("Merging Networks");
                finalNet.merge(network);
            }
            finalNet.join(entity);
            entity.setEnergyNetwork(finalNet);
            entity.markDirty();

        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, ConductiveEnergyDuctBlockEntity entity) {
        if (world.isClient) {
            return;
        }
        if (entity.energyNetwork == null) {
            initNetwork(world, pos, entity);
        }


        List<EnergyStorage> extractFrom = new ArrayList<>();
        List<EnergyStorage> insertTo = new ArrayList<>();

        //Initialize
        for (Direction direction : Direction.values()) {
            EnergyStorage maybeStorage = SimpleEnergyStorage.SIDED.find(world, pos.offset(direction, 1), direction);
            if (maybeStorage != null) {
                //Get extraction containers
                if (maybeStorage.supportsExtraction()) {
                    extractFrom.add(maybeStorage);
                }
                //Get insertion containers and neighboor cables.
                if (maybeStorage.supportsInsertion()) {
                    if (maybeStorage.getAmount() < maybeStorage.getCapacity()) {
                        insertTo.add(maybeStorage);
                    }
                }
            }
        }//Init

//
//            //Insert Energy
        for (EnergyStorage insertTarget : insertTo) {
            EnergyStorageUtil.move(
                    entity.getEnergyNetwork(), // from source
                    insertTarget, // into target
                    maxInsert,
                    null // create a new transaction for this operation
            );
        }

//          //Extract Energy from neighboors
        if (extractFrom.size() > 0) {
            for (EnergyStorage extractTarget : extractFrom) {
                EnergyStorageUtil.move(
                        extractTarget, // from source
                        entity.getEnergyNetwork(), // into target
                        maxExtract,
                        null // create a new transaction for this operation
                );
            }
        }
//        }
    }

    public EnergyNetwork getEnergyNetwork() {
        return energyNetwork;
    }

    public void setEnergyNetwork(EnergyNetwork energyNetwork) {
        this.energyNetwork = energyNetwork;
    }

}
