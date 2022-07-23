package net.fabricmc.scrap.recipe;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.scrap.Main;
import net.fabricmc.scrap.item.ModItemGroup;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModRecipes {
    public static void registerModRecipes(){
        Main.LOGGER.info("Registering Mod Recipes for "+Main.MOD_ID);
        registerRecipes();
    }

    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Main.MOD_ID, OreWasherRecipe.Serializer.ID),
                OreWasherRecipe.Serializer.INSTANCE);

        Registry.register(Registry.RECIPE_TYPE, new Identifier(Main.MOD_ID, OreWasherRecipe.Type.ID),
                OreWasherRecipe.Type.INSTANCE);

        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Main.MOD_ID, CrushingRecipe.Serializer.ID),
                CrushingRecipe.Serializer.INSTANCE);

        Registry.register(Registry.RECIPE_TYPE, new Identifier(Main.MOD_ID, CrushingRecipe.Type.ID),
                CrushingRecipe.Type.INSTANCE);
    }
}
