package jonky.modid.network;

import jonky.modid.component.ModComponents;
import jonky.modid.item.ModItems;
import jonky.modid.network.packet.ToggleToolAbilityPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ModNetwork {

    public static void registerModNetwork() {
        PayloadTypeRegistry.serverboundPlay().register(ToggleToolAbilityPayload.ID, ToggleToolAbilityPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ToggleToolAbilityPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayer player = context.player();
                var stack = player.getMainHandItem();
                if (stack.getItem() != ModItems.HEAVY_PICKAXE && stack.getItem() != ModItems.HEAVY_AXE && stack.getItem() != ModItems.HEAVY_SHOVEL && stack.getItem() != ModItems.HEAVY_HOE)
                    return;

                boolean currentValue = Boolean.TRUE.equals(stack.get(ModComponents.TOOL_ABILITY_TOGGLE_COMPONENT));
                boolean newValue = !currentValue;

                stack.set(ModComponents.TOOL_ABILITY_TOGGLE_COMPONENT, newValue);

                String message = newValue ? "Tool Ability Enabled" : "Tool Ability Disabled";
                player.sendSystemMessage(Component.literal(message));
            });
        });
    }
}