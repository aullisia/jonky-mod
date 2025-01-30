package jonky.modid.component;

import com.mojang.serialization.Codec;
import jonky.modid.Jonky;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static final ComponentType<Integer> BANKNOTE_VALUE_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Jonky.MOD_ID, "banknote_value"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static void registerModComponents() {
        Jonky.LOGGER.info("Registering {} components", Jonky.MOD_ID);
    }
}
