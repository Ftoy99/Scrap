package net.fabricmc.scrap.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.scrap.Main;
import net.fabricmc.scrap.block.custom.*;
import net.fabricmc.scrap.block.ducts.energyducts.ConductiveEnergyDuctBlock;
import net.fabricmc.scrap.item.ModItemGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks{

    public static final Block RAW_SCRAP_BLOCK = registerBlock("raw_scrap_block",new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F)), ModItemGroup.SCRAP);

    public static final Block SCRAP_BLOCK = registerBlock("scrap_block",new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F)), ModItemGroup.SCRAP);

    public static final Block MACHINE_CHASSIS = registerBlock("machine_chassis_block",new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F)), ModItemGroup.SCRAP);

    public static final Block CREATIVE_ENERGY_CELL = registerBlock("creative_energy_cell_block",new Block(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F)), ModItemGroup.SCRAP);

    public static final Block ORE_WASHER_BLOCK = registerBlock("ore_washer_block",new OreWasherBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F)), ModItemGroup.SCRAP);

    public static final Block FURNACE_GENERATOR_BLOCK = registerBlock("furnace_generator_block",new FurnaceGeneratorBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F)), ModItemGroup.SCRAP);

    public static final Block SMELTER_BLOCK = registerBlock("smelter_block",new SmelterBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F)), ModItemGroup.SCRAP);

    public static final Block CRUSHER_BLOCK = registerBlock("crusher_block",new CrusherBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F)), ModItemGroup.SCRAP);

    public static final Block ALLOY_SMELTER_BLOCK = registerBlock("alloy_smelter_block",new AlloySmelterBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(5.0F, 6.0F)), ModItemGroup.SCRAP);

    public static final Block CONDUCTIVE_ENERGY_DUCT_BLOCK = registerBlock("conductive_energy_duct_block",new ConductiveEnergyDuctBlock(0.2500f,FabricBlockSettings.of(Material.GLASS).strength(2.0F, 2.0F)), ModItemGroup.SCRAP);

    public static Block registerBlock(String name, Block block, ItemGroup group){
        registerBlockItem(name,block,group);
        return Registry.register(Registry.BLOCK,new Identifier(Main.MOD_ID,name),block);
    }

    public static Item registerBlockItem(String name , Block block, ItemGroup group){
        return Registry.register(Registry.ITEM,new Identifier(Main.MOD_ID,name),new BlockItem(block,new FabricItemSettings().group(group)));
    }

    public static void registerModBlocks(){
        Main.LOGGER.info("Registering Mod Items for "+Main.MOD_ID);
    }
}
