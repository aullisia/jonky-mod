package jonky.modid;

import jonky.modid.block.ModBlockEntities;
import jonky.modid.block.ModBlocks;
import jonky.modid.component.ModComponents;
import jonky.modid.enchantment.ModEnchantmentEffects;
import jonky.modid.enchantment.ModEnchantments;
import jonky.modid.event.RegisterEvents;
import jonky.modid.item.ModItemGroups;
import jonky.modid.item.ModItems;
import jonky.modid.network.ModNetwork;
import jonky.modid.screen.ModScreens;
import jonky.modid.util.ModCustomTrades;
import jonky.modid.event.ShieldEvents;
import jonky.modid.villager.ModVillagers;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jonky implements ModInitializer {
	public static final String MOD_ID = "jonky";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModNetwork.registerModNetwork();
		ModScreens.registerModScreens();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModComponents.registerModComponents();
		ModEnchantmentEffects.registerEnchantmentEffects();
		ModItems.registerModItems();
		ModVillagers.registerVillagers();
		ModCustomTrades.registerCustomTrades();
		RegisterEvents.register();
		ModItemGroups.registerItemGroups();
		ModEnchantments.modifyEnchantments();

		LOGGER.info("Jonky Mod Initialised");
	}
}