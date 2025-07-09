package jonky.modid.network.packet;

import jonky.modid.Jonky;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ToggleToolAbilityPayload() implements CustomPayload {
    public static final Identifier TOGGLE_TOOL_ABILITY_PAYLOAD_ID = Identifier.of(Jonky.MOD_ID, "toggle_tool_ability");
    public static final CustomPayload.Id<ToggleToolAbilityPayload> ID = new CustomPayload.Id<>(TOGGLE_TOOL_ABILITY_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, ToggleToolAbilityPayload> CODEC = PacketCodec.of((buf, payload) -> {}, buf -> new ToggleToolAbilityPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
