package jonky.modid.component;

import com.mojang.serialization.Codec;
import jonky.modid.Jonky;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.dynamic.Codecs;

import java.util.UUID;

public class ModComponents {
    public static final ComponentType<Integer> BANKNOTE_VALUE_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Jonky.MOD_ID, "banknote_value"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Integer> HEAVY_SHIELD_ENERGY_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Jonky.MOD_ID, "heavy_shield_energy"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Integer> LAST_ATTACKER_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Jonky.MOD_ID, "last_attacker_component"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

//    public static final ComponentType<UUID> LAST_ATTACKER_COMPONENT = Registry.register(
//            Registries.DATA_COMPONENT_TYPE,
//            Identifier.of(Jonky.MOD_ID, "last_attacker_component"),
//            ComponentType.<UUID>builder().codec(Uuids.CODEC).build()
//    );

    public static void registerModComponents() {
        Jonky.LOGGER.info("Registering {} components", Jonky.MOD_ID);
    }
}
