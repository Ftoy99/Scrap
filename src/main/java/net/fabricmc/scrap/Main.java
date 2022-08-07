package net.fabricmc.scrap;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.scrap.block.ModBlocks;
import net.fabricmc.scrap.block.entity.ModBlockEntities;
import net.fabricmc.scrap.item.ModItems;
import net.fabricmc.scrap.recipe.ModRecipes;
import net.fabricmc.scrap.screens.ModScreenHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "scrap";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModRecipes.registerModRecipes();
		ModScreenHandlers.registerModScreens();
	}
}
