package net.fabricmc.scrap.item.custom;

import net.fabricmc.scrap.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpatialMarkerItem extends Item {
    public SpatialMarkerItem(Settings settings) {
        super(settings);
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            ItemStack stack = context.getPlayer().getStackInHand(context.getHand());
            NbtCompound pos1Nbt = stack.getSubNbt("pos1");
            NbtCompound pos2Nbt = stack.getSubNbt("pos2");
            System.out.println(pos1Nbt);
            System.out.println(pos2Nbt);
            if (pos1Nbt == null) {
                Main.LOGGER.info("pos1 is null");
                BlockPos blockPos = context.getBlockPos();
                NbtCompound pos1NbtCompound = new NbtCompound();
                pos1NbtCompound.putInt("x", blockPos.getX());
                pos1NbtCompound.putInt("y", blockPos.getY());
                pos1NbtCompound.putInt("z", blockPos.getZ());
                stack.setSubNbt("pos1", pos1NbtCompound);
                context.getPlayer().sendMessage(Text.of("Position 1 Set"));
                return super.useOnBlock(context);
            } else if (pos2Nbt == null) {
                Main.LOGGER.info("pos2 is null");
                BlockPos blockPos = context.getBlockPos();
                NbtCompound pos2NbtCompound = new NbtCompound();
                pos2NbtCompound.putInt("x", blockPos.getX());
                pos2NbtCompound.putInt("y", blockPos.getY());
                pos2NbtCompound.putInt("z", blockPos.getZ());
                stack.setSubNbt("pos2", pos2NbtCompound);
                context.getPlayer().sendMessage(Text.of("Position 2 Set"));
                return super.useOnBlock(context);
            } else {
                return super.useOnBlock(context);
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            ItemStack stack = user.getStackInHand(hand);
            stack.removeSubNbt("pos1");
            stack.removeSubNbt("pos2");
        }
        return super.use(world, user, hand);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return ((stack.getSubNbt("pos1") != null) && (stack.getSubNbt("pos2") != null));
    }


}
