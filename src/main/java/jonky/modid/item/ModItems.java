package jonky.modid.item;

import com.github.crimsondawn45.fabricshieldlib.lib.object.FabricShieldItem;
import jonky.modid.Jonky;
import jonky.modid.item.custom.BanknoteItem;
import jonky.modid.item.custom.HeavyShieldItem;
import jonky.modid.item.custom.WrenchItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public class ModItems {
    public static final Item BANKNOTE = registerItem("banknote", BanknoteItem::new, new Item.Settings());
    public static final Item WRENCH = registerItem("wrench", WrenchItem::new, new Item.Settings());
    public static final Item HEAVY_SHIELD = registerItem("heavy_shield",
            (settings) -> new HeavyShieldItem(settings, 10, 13, Items.NETHERITE_INGOT),
            new Item.Settings().maxDamage(2500));

    public static final Item HEAVY_GREATSWORD = registerItem(
            "heavy_greatsword",
            settings -> new Item(settings.sword(ModToolMaterials.HEAVY_CORE, 3f, -2.4f)),
            new Item.Settings()
    );
    public static final Item HEAVY_PICKAXE = registerItem(
            "heavy_pickaxe",
            settings -> new Item(settings.pickaxe(ModToolMaterials.HEAVY_CORE, 1f, -2.8f)),
            new Item.Settings()
    );
    public static final Item HEAVY_SHOVEL = registerItem(
            "heavy_shovel",
            settings -> new Item(settings.shovel(ModToolMaterials.HEAVY_CORE, 1.5f, -3f)),
            new Item.Settings()
    );
    public static final Item HEAVY_AXE = registerItem(
            "heavy_axe",
            settings -> new Item(settings.axe(ModToolMaterials.HEAVY_CORE, 6f, -3.2f)),
            new Item.Settings()
    );
    public static final Item HEAVY_HOE = registerItem(
            "heavy_hoe",
            settings -> new Item(settings.hoe(ModToolMaterials.HEAVY_CORE, 0f, -3f)),
            new Item.Settings()
    );

    public static final SmithingTemplateItem HEAVY_UPGRADE = (SmithingTemplateItem) registerItem(
            "heavy_upgrade_smithing_template",
            settings -> new SmithingTemplateItem(
                    Text.translatable("item.jonky.heavy_template.applies_to").formatted(Formatting.GRAY),
                    Text.translatable("item.jonky.heavy_template.ingredients").formatted(Formatting.BLUE),
                    Text.translatable("item.jonky.heavy_template.base_slot_description"),
                    Text.translatable("item.jonky.heavy_template.additions_slot_description"),
                    List.of(Identifier.ofVanilla("container/slot/sword")),
                    List.of(Identifier.ofVanilla("container/slot/ingot")),
                    settings
            ),
            new Item.Settings()
    );

    public static Item registerItem(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Jonky.MOD_ID, path));
        return Items.register(registryKey, factory, settings);
    }

    public static void registerModItems() {
        Jonky.LOGGER.info("Registering Mod Items for" + Jonky.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(HEAVY_SHIELD);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(HEAVY_UPGRADE);
        });
    }
}
