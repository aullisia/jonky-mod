package jonky.modid.network;

import jonky.modid.Jonky;
import jonky.modid.component.ModComponents;
import jonky.modid.item.ModItems;
import jonky.modid.network.packet.ToggleToolAbilityPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.playC2S;

public class ModNetwork {

    public static void registerModNetwork() {
        playC2S().register(ToggleToolAbilityPayload.ID, ToggleToolAbilityPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ToggleToolAbilityPayload.ID, (payload, context) -> {
            context.player().getServer().execute(() -> {
                ServerPlayerEntity player = context.player();
                var stack = player.getStackInHand(player.getActiveHand());
                if (stack.getItem() != ModItems.HEAVY_PICKAXE && stack.getItem() != ModItems.HEAVY_AXE && stack.getItem() != ModItems.HEAVY_SHOVEL && stack.getItem() != ModItems.HEAVY_HOE)
                    return;

                boolean currentValue = Boolean.TRUE.equals(stack.get(ModComponents.TOOL_ABILITY_TOGGLE_COMPONENT));
                boolean newValue = !currentValue;

                stack.set(ModComponents.TOOL_ABILITY_TOGGLE_COMPONENT, newValue);

                String message = newValue ? "Tool Ability Enabled" : "Tool Ability Disabled";
                player.sendMessage(Text.of(message), true);
            });
        });
    }
}
