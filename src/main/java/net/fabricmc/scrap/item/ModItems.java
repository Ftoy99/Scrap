package net.fabricmc.scrap.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.scrap.Main;
import net.fabricmc.scrap.item.custom.DebugToolItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModItems {
    public static final Item SCRAP =registerItem("scrap",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item SCRAP_INGOT =registerItem("scrap_ingot",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item WASHED_RAW_IRON =registerItem("washed_raw_iron",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item WASHED_RAW_GOLD =registerItem("washed_raw_gold",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item WASHED_RAW_COPPER =registerItem("washed_raw_copper",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item DEBUG_TOOL =registerItem("debug_tool",new DebugToolItem(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(1)));
    public static final Item SCRAP_NUGGET =registerItem("scrap_nugget",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item CONDUCTIVE_ALLOY_INGOT =registerItem("conductive_alloy_ingot",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item COAL_DUST =registerItem("coal_dust",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item LAPIS_LAZULI_DUST =registerItem("lapis_lazuli_dust",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));
    public static final Item ENDER_DUST =registerItem("ender_dust",new Item(new FabricItemSettings().group(ModItemGroup.SCRAP).maxCount(64)));



    public static void registerModItems(){
        Main.LOGGER.info("Registering Mod Items for "+Main.MOD_ID);
    }
    public static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM,new Identifier(Main.MOD_ID,name),item);
    }
}
