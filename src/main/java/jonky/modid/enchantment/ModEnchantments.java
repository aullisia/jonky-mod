package jonky.modid.enchantment;

import jonky.modid.Jonky;
import jonky.modid.enchantment.custom.ForsakingEnchantmentEffect;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> FORSAKING =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Jonky.MOD_ID, "forsaking"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        RegistryEntryList<Enchantment> forsakingExclusiveSet = RegistryEntryList.of(
                enchantments.getOrThrow(Enchantments.LOYALTY)
        );

        register(registerable, FORSAKING, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(25, 9),
                        2,
                        AttributeModifierSlot.HAND))
                .exclusiveSet(forsakingExclusiveSet)
                .addEffect(EnchantmentEffectComponentTypes.HIT_BLOCK,
                        new ForsakingEnchantmentEffect()));
    }

    private static void modifyEnchantments() {
        EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, enchantingContext) -> {
            if (enchantment == Enchantments.THORNS && target.getItem() == Items.SHIELD) {
                return TriState.TRUE; // Allow enchanting
            }
            if (enchantment == Enchantments.KNOCKBACK && target.getItem() == Items.SHIELD) {
                return TriState.TRUE; // Allow enchanting
            }

            return TriState.DEFAULT;
        });
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
        modifyEnchantments();
    }
}
