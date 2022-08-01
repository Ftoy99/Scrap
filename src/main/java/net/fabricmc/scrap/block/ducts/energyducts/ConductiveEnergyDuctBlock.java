package net.fabricmc.scrap.block.ducts.energyducts;

import net.fabricmc.scrap.block.entity.ModBlockEntities;
import net.fabricmc.scrap.block.entity.ducts.energyducts.ConductiveEnergyDuctEntity;
import net.fabricmc.scrap.block.entity.ducts.energyducts.EnergyNetwork;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ConductiveEnergyDuctBlock extends EnergyDuct {
    public ConductiveEnergyDuctBlock(float radius, Settings settings) {
        super(radius, settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConductiveEnergyDuctEntity(pos,state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient()){
            BlockEntity maybeCable = world.getBlockEntity(pos);
            if (maybeCable != null) {
                if (maybeCable instanceof ConductiveEnergyDuctEntity){
                    ConductiveEnergyDuctEntity cable = (ConductiveEnergyDuctEntity) maybeCable;
                    cable.initializeNetwork();
                }
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (!world.isClient()){
            BlockEntity maybeCable = world.getBlockEntity(pos);
            if (maybeCable != null) {
                if (maybeCable instanceof ConductiveEnergyDuctEntity){
                    ConductiveEnergyDuctEntity cable = (ConductiveEnergyDuctEntity) maybeCable;
                    EnergyNetwork  net = cable.getEnergyNetwork();
                    net.quit(cable.getPos());
                    net.split(world,pos);
                }
            }
        }


    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.CONDUCTIVE_ENERGY_DUCT_ENTITY, ConductiveEnergyDuctEntity::tick);
    }
}
