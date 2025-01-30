package jonky.modid.enchantment;

import com.mojang.serialization.MapCodec;
import jonky.modid.Jonky;
import jonky.modid.enchantment.custom.ForsakingEnchantmentEffect;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
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
