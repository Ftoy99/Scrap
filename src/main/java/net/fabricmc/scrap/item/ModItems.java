package net.fabricmc.scrap.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.scrap.Main;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class ModItems {
    public static final Item SCRAP =registerItem("scrap",new Item(new FabricItemSettings().group(ItemGroup.MATERIALS).maxCount(128)));
    public static void registerModItems(){
        Main.LOGGER.info("Registering Mod Items for "+Main.MOD_ID);
    }
    public static Item registerItem(String name, Item item){
        return Registry.register(Registry.ITEM,new Identifier(Main.MOD_ID,name),item);
    }
}
