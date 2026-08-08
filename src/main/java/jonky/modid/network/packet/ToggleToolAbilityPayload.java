package jonky.modid.network.packet;

import jonky.modid.Jonky;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ToggleToolAbilityPayload() implements CustomPacketPayload {
    public static final Identifier TOGGLE_TOOL_ABILITY_PAYLOAD_ID = Identifier.fromNamespaceAndPath(Jonky.MOD_ID, "toggle_tool_ability");
    public static final CustomPacketPayload.Type<ToggleToolAbilityPayload> ID = new CustomPacketPayload.Type<>(TOGGLE_TOOL_ABILITY_PAYLOAD_ID);
    public static final StreamCodec<FriendlyByteBuf, ToggleToolAbilityPayload> CODEC = StreamCodec.ofMember((buf, payload) -> {}, buf -> new ToggleToolAbilityPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}