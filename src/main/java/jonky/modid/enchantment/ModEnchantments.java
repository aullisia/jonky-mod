package jonky.modid.enchantment;

import jonky.modid.Jonky;
import jonky.modid.enchantment.custom.ForsakingEnchantmentEffect;
import jonky.modid.item.ModItems;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.data.worldgen.BootstrapContext;

import java.util.Optional;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> FORSAKING =
            ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Jonky.MOD_ID, "forsaking"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        register(context, FORSAKING, Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                                5,
                                1,
                                Enchantment.dynamicCost(5, 7),
                                Enchantment.dynamicCost(25, 9),
                                2,
                                EquipmentSlotGroup.HAND))
                .exclusiveWith(HolderSet.direct(enchantments.getOrThrow(Enchantments.LOYALTY)))
                .withEffect(EnchantmentEffectComponents.HIT_BLOCK,
                        new ForsakingEnchantmentEffect()));
    }

    public static void modifyEnchantments() {
        EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, stack, context) -> {
            Optional<ResourceKey<Enchantment>> keyOptional = enchantment.unwrapKey();

            if (keyOptional.isPresent() && keyOptional.get().equals(Enchantments.KNOCKBACK)
                    && (stack.is(Items.SHIELD) || stack.is(ModItems.HEAVY_SHIELD))) {
                return TriState.TRUE;
            }

            return TriState.DEFAULT;
        });
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }
}