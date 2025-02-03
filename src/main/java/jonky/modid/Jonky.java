package jonky.modid;

import jonky.modid.block.ModBlockEntities;
import jonky.modid.block.ModBlocks;
import jonky.modid.block.custom.ATM.ATMScreenHandler;
import jonky.modid.component.ModComponents;
import jonky.modid.enchantment.ModEnchantmentEffects;
import jonky.modid.item.ModItems;
import jonky.modid.screen.ModScreens;
import jonky.modid.util.ModCustomTrades;
import jonky.modid.util.ShieldEvents;
import jonky.modid.villager.ModVillagers;
import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jonky implements ModInitializer {
	public static final String MOD_ID = "jonky";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModScreens.registerModScreens();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModComponents.registerModComponents();
		ModEnchantmentEffects.registerEnchantmentEffects();
		ModItems.registerModItems();
		ModVillagers.registerVillagers();
		ModCustomTrades.registerCustomTrades();
		ShieldEvents.registerShieldEvents();

		LOGGER.info("Jonky Mod Initialised");
	}
}