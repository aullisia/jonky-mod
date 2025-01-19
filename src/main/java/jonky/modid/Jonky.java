package jonky.modid;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jonky implements ModInitializer {
	public static final String MOD_ID = "jonky";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Jonky Mod Initialised!");
	}
}