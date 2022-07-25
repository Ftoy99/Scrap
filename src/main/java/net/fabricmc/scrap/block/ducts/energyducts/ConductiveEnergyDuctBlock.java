package net.fabricmc.scrap.block.ducts.energyducts;

import net.fabricmc.scrap.block.ModBlocks;
import net.fabricmc.scrap.block.ducts.Ducts;
import net.fabricmc.scrap.block.entity.ModBlockEntities;
import net.fabricmc.scrap.block.entity.ducts.energyducts.ConductiveEnergyDuctBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import javax.annotation.Nullable;


public class ConductiveEnergyDuctBlock extends Ducts {
    public ConductiveEnergyDuctBlock(float radius, Settings settings) {
        super(radius, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false).with(DOWN, false));
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.withConnectionProperties(ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (!world.isClient()) {
            //Find the network the entity was and quit it or split it.
            for (Direction direction : Direction.values()){
                BlockEntity possibleCable = world.getBlockEntity(pos.offset(direction,1));
                if (possibleCable!=null && possibleCable instanceof ConductiveEnergyDuctBlockEntity){
                    ((ConductiveEnergyDuctBlockEntity) possibleCable).getEnergyNetwork().split(pos,(World)world);
                    break;
                }
            }
        }
        super.onBroken(world, pos, state);
    }

    public BlockState withConnectionProperties(World world, BlockPos pos) {
        //TODO refactor this somehow
        BlockPos pos1 = pos.down();
        BlockPos pos2 = pos.up();
        BlockPos pos3 = pos.north();
        BlockPos pos4 = pos.east();
        BlockPos pos5 = pos.south();
        BlockPos pos6 = pos.west();
        BlockState blockState = world.getBlockState(pos1);
        BlockState blockState2 = world.getBlockState(pos2);
        BlockState blockState3 = world.getBlockState(pos3);
        BlockState blockState4 = world.getBlockState(pos4);
        BlockState blockState5 = world.getBlockState(pos5);
        BlockState blockState6 = world.getBlockState(pos6);
        return this.getDefaultState().with(DOWN, blockState.isOf(this) || canConnect(pos1, world)).with(UP, blockState2.isOf(this) || canConnect(pos2, world)).with(NORTH, blockState3.isOf(this) || canConnect(pos3, world)).with(EAST, blockState4.isOf(this) || canConnect(pos4, world)).with(SOUTH, blockState5.isOf(this) || canConnect(pos5, world)).with(WEST, blockState6.isOf(this) || canConnect(pos6, world));
    }

    public boolean canConnect(BlockPos pos, World world) {
        @Nullable
        EnergyStorage maybeStorage = SimpleEnergyStorage.SIDED.find(world, pos, null);
        return maybeStorage != null || world.getBlockState(pos).isOf(this);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            world.createAndScheduleBlockTick(pos, this, 1);
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
        boolean bl = neighborState.isOf(this) || neighborState.isOf(ModBlocks.CONDUCTIVE_ENERGY_DUCT_BLOCK) || canConnect(neighborPos, (World) world);
        return state.with(FACING_PROPERTIES.get(direction), bl);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConductiveEnergyDuctBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.CONDUCTIVE_ENERGY_DUCT_BLOCK_ENTITY, ConductiveEnergyDuctBlockEntity::tick);
    }
}
