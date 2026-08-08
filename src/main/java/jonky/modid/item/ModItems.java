package jonky.modid.item;

import jonky.modid.Jonky;
import jonky.modid.component.ModComponents;
import jonky.modid.item.custom.BanknoteItem;
import jonky.modid.item.custom.HeavyShieldItem;
import jonky.modid.item.custom.WrenchItem;
import jonky.modid.item.custom.heavy.tools.HeavyAxe;
import jonky.modid.item.custom.heavy.tools.HeavyHoe;
import jonky.modid.item.custom.heavy.tools.HeavyPickaxe;
import jonky.modid.item.custom.heavy.tools.HeavyShovel;
import jonky.modid.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.component.BlocksAttacks;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ModItems {
    public static final Item BANKNOTE = registerItem("banknote", BanknoteItem::new, new Item.Properties());
    public static final Item WRENCH = registerItem("wrench", WrenchItem::new, new Item.Properties());
    public static final Item HEAVY_SHIELD = registerItem("heavy_shield",
            HeavyShieldItem::new,
            new Item.Properties()
                    .durability(2500)
                    .enchantable(13)
                    .repairable(ModTags.Items.HEAVY_CORE_REPAIR)
                    .equippableUnswappable(EquipmentSlot.OFFHAND)
                    .delayedComponent(
                            DataComponents.BLOCKS_ATTACKS,
                            context -> new BlocksAttacks(
                                    0.4F,
                                    1.0F,
                                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                                    Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                                    Optional.of(SoundEvents.SHIELD_BLOCK),
                                    Optional.of(SoundEvents.SHIELD_BREAK)
                            )
                    )
                    .component(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT, 0)
    );

    public static final Item HEAVY_GREATSWORD = registerItem(
            "heavy_greatsword",
            settings -> new Item(settings.sword(ModToolMaterials.HEAVY_CORE, 3.5f, -2.4f)),
            new Item.Properties()
    );
    public static final Item HEAVY_PICKAXE = registerItem(
            "heavy_pickaxe", settings -> new HeavyPickaxe(ModToolMaterials.HEAVY_CORE, 1f, -2.8f, settings), new Item.Properties()
    );
    public static final Item HEAVY_SHOVEL = registerItem(
            "heavy_shovel", settings -> new HeavyShovel(ModToolMaterials.HEAVY_CORE, 1.5f, -3f, settings), new Item.Properties()
    );
    public static final Item HEAVY_AXE = registerItem(
            "heavy_axe", settings -> new HeavyAxe(ModToolMaterials.HEAVY_CORE, 7f, -3f, settings), new Item.Properties()
    );

    public static final Item HEAVY_HOE = registerItem(
            "heavy_hoe", settings -> new HeavyHoe(ModToolMaterials.HEAVY_CORE, -5f, 1f, settings), new Item.Properties()
    );
    public static final SmithingTemplateItem HEAVY_UPGRADE = (SmithingTemplateItem) registerItem(
            "heavy_upgrade_smithing_template",
            settings -> new SmithingTemplateItem(
                    Component.translatable("item.jonky.heavy_template.applies_to").withStyle(ChatFormatting.BLUE),
                    Component.translatable("item.jonky.heavy_template.ingredients").withStyle(ChatFormatting.BLUE),
                    Component.translatable("item.jonky.heavy_template.base_slot_description"),
                    Component.translatable("item.jonky.heavy_template.additions_slot_description"),
                    List.of(Identifier.withDefaultNamespace("container/slot/sword")),
                    List.of(Identifier.withDefaultNamespace("container/slot/ingot")),
                    settings
            ),
            new Item.Properties()
    );

    public static Item registerItem(String path, Function<Item.Properties, Item> factory, Item.Properties settings) {
        final ResourceKey<Item> registryKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Jonky.MOD_ID, path));
        Item item = factory.apply(settings.setId(registryKey));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }
        return Registry.register(BuiltInRegistries.ITEM, registryKey, item);
    }

    public static void registerModItems() {
        Jonky.LOGGER.info("Registering Mod Items for" + Jonky.MOD_ID);
    }
}