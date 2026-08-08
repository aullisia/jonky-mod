package jonky.modid.component;

import com.mojang.serialization.Codec;
import jonky.modid.Jonky;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModComponents {
    public static final DataComponentType<Integer> BANKNOTE_VALUE_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Jonky.MOD_ID, "banknote_value"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    public static final DataComponentType<Integer> HEAVY_SHIELD_ENERGY_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Jonky.MOD_ID, "heavy_shield_energy"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    public static final DataComponentType<Integer> LAST_ATTACKER_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Jonky.MOD_ID, "last_attacker_component"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    public static final DataComponentType<Boolean> TOOL_ABILITY_TOGGLE_COMPONENT = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Jonky.MOD_ID, "tool_ability_toggle_component"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static void registerModComponents() {
        Jonky.LOGGER.info("Registering {} components", Jonky.MOD_ID);
    }
}