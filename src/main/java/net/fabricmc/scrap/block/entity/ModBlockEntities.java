package net.fabricmc.scrap.block.entity;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.scrap.Main;
import net.fabricmc.scrap.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.api.EnergyStorage;


public class ModBlockEntities {


    public static BlockEntityType<OreWasherBlockEntity> ORE_WASHER_BLOCK_ENTITY;
    public static BlockEntityType<FurnaceGeneratorBlockEntity> FURNACE_GENERATOR_BLOCK_ENTITY;

    public static void registerBlockEntities() {
        ORE_WASHER_BLOCK_ENTITY =
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "ore_washer_block"),
                        FabricBlockEntityTypeBuilder.create(OreWasherBlockEntity::new,
                                ModBlocks.ORE_WASHER_BLOCK).build(null));
        FURNACE_GENERATOR_BLOCK_ENTITY =
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MOD_ID, "furnace_generator_block"),
                        FabricBlockEntityTypeBuilder.create(FurnaceGeneratorBlockEntity::new,
                                ModBlocks.FURNACE_GENERATOR_BLOCK).build(null));

    }
}
