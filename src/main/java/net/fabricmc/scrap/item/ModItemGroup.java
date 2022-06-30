package net.fabricmc.scrap.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.scrap.Main;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup SCRAP = FabricItemGroupBuilder.build(new Identifier(Main.MOD_ID,"scrap"),() -> new ItemStack(ModItems.SCRAP));
}
