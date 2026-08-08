package jonky.modid;

import jonky.modid.block.custom.ATM.ATMScreen;
import jonky.modid.event.KeyInputHandler;
import jonky.modid.screen.ModScreens;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class JonkyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(ModScreens.ATM_SCREEN_HANDLER, ATMScreen::new);
        KeyInputHandler.register();
    }
}