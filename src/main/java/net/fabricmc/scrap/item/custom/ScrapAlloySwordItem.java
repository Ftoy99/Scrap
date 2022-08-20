package net.fabricmc.scrap.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleBatteryItem;

import java.util.List;

public class ScrapAlloySwordItem extends SwordItem implements SimpleBatteryItem {
    public ScrapAlloySwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public long getEnergyCapacity() {
        return 100000;
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
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        tryUseEnergy(stack, 100);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of("Energy:" + SimpleBatteryItem.getStoredEnergyUnchecked(stack) + "/" + getEnergyCapacity()));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
