package jonky.modid.screen;

import jonky.modid.Jonky;
import jonky.modid.block.custom.ATM.ATMScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.flag.FeatureFlags;

public class ModScreens {
    public static final MenuType<ATMScreenHandler> ATM_SCREEN_HANDLER = Registry.register(
            BuiltInRegistries.MENU,
            Identifier.fromNamespaceAndPath(Jonky.MOD_ID, "atm_block"),
            new MenuType<>(ATMScreenHandler::new, FeatureFlags.VANILLA_SET)
    );

    public static void registerModScreens() {
        Jonky.LOGGER.info("Registering Mod Screens for " + Jonky.MOD_ID);
    }
}