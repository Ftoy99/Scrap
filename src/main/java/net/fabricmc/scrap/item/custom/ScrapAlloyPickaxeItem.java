package net.fabricmc.scrap.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleBatteryItem;

import java.util.List;

public class ScrapAlloyPickaxeItem extends PickaxeItem implements SimpleBatteryItem {
    public ScrapAlloyPickaxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }


    @Override
    public long getEnergyCapacity() {
        return 100000;
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (getStoredEnergy(stack) >= 100) {
            return super.getMiningSpeedMultiplier(stack, state);
        } else return 1.0F;
    }

    @Override
    public long getEnergyMaxInput() {
        return 100;
    }

    @Override
    public long getEnergyMaxOutput() {
        return 0;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int step = Math.round((((float) this.getStoredEnergy(stack))/(float) this.getEnergyCapacity())*13);
        return step;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathHelper.packRgb(0, 100, 130);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
            tryUseEnergy(stack, 100);
        }
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of("Energy:" + SimpleBatteryItem.getStoredEnergyUnchecked(stack) + "/" + getEnergyCapacity()));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
