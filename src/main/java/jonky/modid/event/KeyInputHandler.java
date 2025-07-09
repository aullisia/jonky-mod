package jonky.modid.event;

import jonky.modid.network.packet.ToggleToolAbilityPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_JONKY = "key.category.jonky";
    public static final String KEY_TOGGLE_TOOL_ABILITY = "key.jonky.toggle_tool_ability";

    public static KeyBinding toggleToolAbilityKey;

    private static final int COOLDOWN_TICKS = 5;
    private static int cooldownTimer = 0;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if (cooldownTimer > 0) {
                cooldownTimer--;
            }

            if (toggleToolAbilityKey.isPressed() && cooldownTimer == 0) {
                ClientPlayNetworking.send(new ToggleToolAbilityPayload());
                cooldownTimer = COOLDOWN_TICKS;
            }
        });
    }

    public static void register() {
        toggleToolAbilityKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_TOGGLE_TOOL_ABILITY,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY_JONKY
        ));

        registerKeyInputs();
    }
}
