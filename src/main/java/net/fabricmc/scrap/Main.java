package net.fabricmc.scrap;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.scrap.block.ModBlocks;
import net.fabricmc.scrap.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "scrap";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
	}
}
