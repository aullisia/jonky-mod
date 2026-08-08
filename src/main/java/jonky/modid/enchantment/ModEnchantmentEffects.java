package jonky.modid.enchantment;

import com.mojang.serialization.MapCodec;
import jonky.modid.Jonky;
import jonky.modid.enchantment.custom.ForsakingEnchantmentEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class ModEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> FORSAKING =
            registerEntityEffect("forsaking", ForsakingEnchantmentEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name,
                                                                                    MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.fromNamespaceAndPath(Jonky.MOD_ID, name), codec);
    }

    public static void registerEnchantmentEffects() {
        Jonky.LOGGER.info("Registering Mod Enchantment Effects for " + Jonky.MOD_ID);
    }
}