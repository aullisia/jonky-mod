package jonky.modid.enchantment;

import com.mojang.serialization.MapCodec;
import jonky.modid.Jonky;
import jonky.modid.enchantment.custom.ForsakingEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> Forsaking =
            registerEntityEffect("forsaking", ForsakingEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name,
                                                                                    MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(Jonky.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        Jonky.LOGGER.info("Registering Mod Enchantment Effects for " + Jonky.MOD_ID);
    }
}
