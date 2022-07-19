package net.fabricmc.scrap.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class DebugToolItem extends Item {
    public DebugToolItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()){
            EnergyStorage maybeStorage = SimpleEnergyStorage.SIDED.find(context.getWorld(),context.getBlockPos(),null);
            if (maybeStorage!=null){
                context.getPlayer().sendMessage(Text.of("SU:"+maybeStorage.getAmount()+"/"+maybeStorage.getCapacity()));}
        }
        return super.useOnBlock(context);
    }
}
