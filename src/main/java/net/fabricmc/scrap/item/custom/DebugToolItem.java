package net.fabricmc.scrap.item.custom;

import net.fabricmc.scrap.block.entity.ModBlockEntities;
import net.fabricmc.scrap.block.entity.ducts.energyducts.ConductiveEnergyDuctEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class DebugToolItem extends Item {
    public DebugToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        if (!world.isClient()) {
            EnergyStorage maybeStorage = SimpleEnergyStorage.SIDED.find(world, pos, null);
            if (maybeStorage != null) {
                context.getPlayer().sendMessage(Text.of("SU:" + maybeStorage.getAmount() + "/" + maybeStorage.getCapacity()));
            }
            BlockEntity maybeCable = world.getBlockEntity(pos);
            if (maybeCable!=null && maybeCable.getType()== ModBlockEntities.CONDUCTIVE_ENERGY_DUCT_ENTITY){
                ConductiveEnergyDuctEntity cable =  (ConductiveEnergyDuctEntity) maybeCable;
                context.getPlayer().sendMessage(Text.of("Master:"+cable.isMaster()+", Network:"+cable.energyNetwork));
                System.out.println("Cable:"+cable);
                System.out.println("Cable Network:"+cable.energyNetwork.cables);
            }
        }


        return super.useOnBlock(context);
    }
}
