package net.fabricmc.scrap.item.custom;

import net.fabricmc.scrap.block.entity.ducts.energyducts.ConductiveEnergyDuctBlockEntity;
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
        if (!world.isClient()){
            EnergyStorage maybeStorage = SimpleEnergyStorage.SIDED.find(world,pos,null);
            if (maybeStorage!=null){
                context.getPlayer().sendMessage(Text.of("SU:"+maybeStorage.getAmount()+"/"+maybeStorage.getCapacity()));
            }else {
                ConductiveEnergyDuctBlockEntity maybeCable;
                if (world.getBlockEntity(pos) instanceof ConductiveEnergyDuctBlockEntity){
                    maybeCable = (ConductiveEnergyDuctBlockEntity) world.getBlockEntity(pos);
                    context.getPlayer().sendMessage(Text.of("Id : "+maybeCable.getEnergyNetwork().getId()+" Energy:"+maybeCable.getEnergyNetwork().getAmount() +"/"+maybeCable.getEnergyNetwork().getCapacity()+"Cables : "+maybeCable.getEnergyNetwork().getSize()));
                }
            }
        }


        return super.useOnBlock(context);
    }
}
