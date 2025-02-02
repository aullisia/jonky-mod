package jonky.modid;

import com.github.crimsondawn45.fabricshieldlib.lib.event.ShieldBlockCallback;
import jonky.modid.component.ModComponents;
import jonky.modid.enchantment.ModEnchantmentEffects;
import jonky.modid.item.ModItems;
import jonky.modid.util.ModCustomTrades;
import jonky.modid.util.ShieldEvents;
import jonky.modid.villager.ModVillagers;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jonky implements ModInitializer {
	public static final String MOD_ID = "jonky";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModComponents.registerModComponents();
		ModEnchantmentEffects.registerEnchantmentEffects();
		ModItems.registerModItems();
		ModVillagers.registerVillagers();
		ModCustomTrades.registerCustomTrades();
		ShieldEvents.registerShieldEvents();

		LOGGER.info("Jonky Mod Initialised");
	}
}