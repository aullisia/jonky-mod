package jonky.modid.screen;

import jonky.modid.Jonky;
import jonky.modid.block.custom.ATM.ATMScreenHandler;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreens {
    public static final ScreenHandlerType<ATMScreenHandler> ATM_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Jonky.MOD_ID, "atm_block"), new ScreenHandlerType<>(ATMScreenHandler::new, FeatureSet.empty()));

    public static void registerModScreens() {
        Jonky.LOGGER.info("Registering Mod Screens for" + Jonky.MOD_ID);
    }
}
