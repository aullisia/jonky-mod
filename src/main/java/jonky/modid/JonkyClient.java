package jonky.modid;

import jonky.modid.block.custom.ATM.ATMScreen;
import jonky.modid.screen.ModScreens;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class JonkyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreens.ATM_SCREEN_HANDLER, ATMScreen::new);
    }
}
