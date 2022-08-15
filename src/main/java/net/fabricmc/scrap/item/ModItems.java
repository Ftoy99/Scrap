package net.fabricmc.scrap.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.scrap.Main;
import net.fabricmc.scrap.item.custom.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModItems {
    //Ingots

    //Scrap
    public static final Item SCRAP =registerItem("scrap",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item SCRAP_INGOT =registerItem("scrap_ingot",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item SCRAP_NUGGET =registerItem("scrap_nugget",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));

    //Refined Scrap
    public static final Item REFINED_SCRAP_INGOT =registerItem("refined_scrap_ingot",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));

    //Scrap Alloy Ingot
    public static final Item SCRAP_ALLOY_INGOT =registerItem("scrap_alloy_ingot",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));


    //Conductive Alloy
    public static final Item CONDUCTIVE_ALLOY_INGOT =registerItem("conductive_alloy_ingot",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));



    //Steel Alloy
    public static final Item STEEL_ALLOY_INGOT =registerItem("steel_alloy_ingot",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));


    //Washed ores
    public static final Item WASHED_RAW_COPPER =registerItem("washed_raw_copper",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item WASHED_RAW_GOLD =registerItem("washed_raw_gold",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item WASHED_RAW_IRON =registerItem("washed_raw_iron",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));


    //Uncharged Super Conductive
    public static final Item UNCHARGED_SUPER_CONDUCTIVE_INGOT =registerItem("uncharged_super_conductive_ingot",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));


    //Charged Super Conductive
    public static final Item CHARGED_SUPER_CONDUCTIVE_INGOT =registerItem("charged_super_conductive_ingot",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));

    //Crafting Items
    public static final Item STEEL_STICK =registerItem("steel_stick",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item BASIC_CAPACITOR =registerItem("basic_capacitor",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(1)));

    //Dusts
    public static final Item COAL_DUST =registerItem("coal_dust",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item REFINING_DUST =registerItem("refining_dust",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item LAPIS_LAZULI_DUST =registerItem("lapis_lazuli_dust",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item ENDER_DUST =registerItem("ender_dust",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));

    //Random Tools
    public static final Item POUCH =registerItem("pouch",new PouchItem(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(1)));
    public static final Item DEBUG_TOOL =registerItem("debug_tool",new DebugToolItem(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(1)));


    //Tools
    public static final Item SCRAP_ALLOY_SWORD =registerItem("scrap_alloy_sword",new ScrapAlloySwordItem(ModToolMaterials.SCRAP_ALLOY,3,-2.4F,new FabricItemSettings().group(ModItemGroup.SCRAP).fireproof()));
    public static final Item SCRAP_ALLOY_SHOVEL =registerItem("scrap_alloy_shovel",new ScrapAlloyShovelItem(ModToolMaterials.SCRAP_ALLOY, 1.5F, -3.0F,new FabricItemSettings().group(ModItemGroup.SCRAP).fireproof()));
    public static final Item SCRAP_ALLOY_HOE =registerItem("scrap_alloy_hoe",new ScrapAlloyHoeItem(ModToolMaterials.SCRAP_ALLOY,-4, 0.0F,new FabricItemSettings().group(ModItemGroup.SCRAP).fireproof()));
    public static final Item SCRAP_ALLOY_PICKAXE =registerItem("scrap_alloy_pickaxe",new ScrapAlloyPickaxeItem(ModToolMaterials.SCRAP_ALLOY, 1, -2.8F, new FabricItemSettings().group(ModItemGroup.SCRAP).fireproof()));
    public static final Item SCRAP_ALLOY_AXE =registerItem("scrap_alloy_axe",new ScrapAlloyAxeItem(ModToolMaterials.SCRAP_ALLOY, 5.0F, -3.0F,new FabricItemSettings().group(ModItemGroup.SCRAP).fireproof()));

    public static void registerModItems(){
        Main.LOGGER.info("Registering Mod Items for "+Main.MOD_ID);
    }

    public static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM,new Identifier(Main.MOD_ID,name),item);
    }
}
