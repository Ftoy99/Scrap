package net.fabricmc.scrap.block.entity.ducts.energyducts;

import net.fabricmc.scrap.block.ModBlocks;
import net.fabricmc.scrap.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class EnergyDuctBlockEntity extends BlockEntity {
    public static final int maxExtract = 64;
    public static final int maxInsert = 64;

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(512, maxInsert, maxExtract) {
        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };
    protected final PropertyDelegate propertyDelegate;

    public EnergyDuctBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENERGY_DUCT_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return (int) EnergyDuctBlockEntity.this.energyStorage.amount;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        EnergyDuctBlockEntity.this.energyStorage.amount = value;
                        break;
                    default:
                        break;
                }
            }

            public int size() {
                return 1;
            }
        };
    }

    public static void tick(World world, BlockPos pos, BlockState state, EnergyDuctBlockEntity entity) {
        EnergyStorage myStorage = SimpleEnergyStorage.SIDED.find(world, pos, null);
        if (myStorage != null) {
            List<EnergyStorage> extractFrom = new ArrayList<>();
            List<EnergyStorage> insertMachine = new ArrayList<>();
            List<EnergyStorage> insertCable = new ArrayList<>();

            //Initialize
            for (Direction direction : Direction.values()) {
                EnergyStorage maybeStorage = SimpleEnergyStorage.SIDED.find(world, pos.offset(direction, 1), direction);
                if (maybeStorage != null) {
                    //Get extraction containers
                    if (world.getBlockState(pos.offset(direction, 1)).getBlock() != ModBlocks.ENERGY_DUCT_BLOCK) {
                        if (maybeStorage.supportsExtraction()) {
                            extractFrom.add(maybeStorage);
                        }
                    }
                    //Get insertion containers and neighboor cables.
                    if (maybeStorage.supportsInsertion()) {
                        if (world.getBlockState(pos.offset(direction, 1)).getBlock() == ModBlocks.ENERGY_DUCT_BLOCK) {
                            if ((maybeStorage.getAmount() < maybeStorage.getCapacity()) && (maybeStorage.getAmount() < myStorage.getAmount())) {
                                insertCable.add(maybeStorage);
                            }
                        } else {
                            if (maybeStorage.getAmount() < maybeStorage.getCapacity()) {
                                insertMachine.add(maybeStorage);
                            }
                        }
                    }
                }
            }

            //Insert Energy
            int insertTargets = insertMachine.size() + insertCable.size();
            if (insertTargets > 0) {
                for (EnergyStorage insertTarget : insertMachine) {
                    EnergyStorageUtil.move(
                            myStorage, // from source
                            insertTarget, // into target
                            maxInsert,
                            null // create a new transaction for this operation
                    );
                }

                for (EnergyStorage insertTarget : insertCable) {
                    if ((insertTarget.getAmount() + maxInsert) > (myStorage.getAmount() - maxInsert)) {
                        long insertAmount = (myStorage.getAmount()-insertTarget.getAmount())/2;
                        if (insertAmount>0){
                            EnergyStorageUtil.move(
                                    myStorage, // from source
                                    insertTarget, // into target
                                    insertAmount,
                                    null // create a new transaction for this operation
                            );
                        }
                    } else {
                        EnergyStorageUtil.move(
                                myStorage, // from source
                                insertTarget, // into target
                                maxInsert,
                                null // create a new transaction for this operation
                        );
                    }

                }
            }//Insert Energy

            //Extract Energy from neighboors
            if (extractFrom.size() > 0) {
                for (EnergyStorage extractTarget : extractFrom) {
                    EnergyStorageUtil.move(
                            extractTarget, // from source
                            myStorage, // into target
                            maxExtract,
                            null // create a new transaction for this operation
                    );
                }
            }//Extract Energy
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("energyduct.amount", (int) energyStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        energyStorage.amount = nbt.getInt("energyduct.amount");

    }
}
