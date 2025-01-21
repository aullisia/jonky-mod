package jonky.modid;

import jonky.modid.block.ModBlocks;
import jonky.modid.component.ModDataComponentTypes;
import jonky.modid.item.ModItems;
import jonky.modid.util.ModCustomTrades;
import jonky.modid.villager.ModVillagers;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jonky implements ModInitializer {
	public static final String MOD_ID = "jonky";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModDataComponentTypes.registerDataComponentTypes();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModVillagers.registerVillagers();
		ModCustomTrades.registerCustomTrades();
		LOGGER.info("Jonky Mod Initialised!");
	}
}