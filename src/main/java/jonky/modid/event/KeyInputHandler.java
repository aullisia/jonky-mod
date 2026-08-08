package jonky.modid.event;

import jonky.modid.Jonky;
import jonky.modid.network.packet.ToggleToolAbilityPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;

public class KeyInputHandler {
    public static final Identifier KEY_CATEGORY_JONKY = Identifier.fromNamespaceAndPath(Jonky.MOD_ID, "key_category");
    public static final String KEY_TOGGLE_TOOL_ABILITY = "key.jonky.toggle_tool_ability";

    public static KeyMapping toggleToolAbilityKey;

    private static boolean wasKeyDown = false;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            boolean isDown = toggleToolAbilityKey.isDown();

            if (isDown && !wasKeyDown) {
                ClientPlayNetworking.send(new ToggleToolAbilityPayload());
            }

            wasKeyDown = isDown;
        });
    }

    public static void register() {
        toggleToolAbilityKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                KEY_TOGGLE_TOOL_ABILITY,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                new KeyMapping.Category(KEY_CATEGORY_JONKY)
        ));

        registerKeyInputs();
    }
}