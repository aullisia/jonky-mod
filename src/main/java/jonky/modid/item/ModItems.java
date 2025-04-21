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
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static final Item BANKNOTE = registerItem("banknote", BanknoteItem::new, new Item.Settings());
    public static final Item WRENCH = registerItem("wrench", WrenchItem::new, new Item.Settings());
    public static final Item HEAVY_SHIELD = registerItem("heavy_shield",
        (settings) -> new HeavyShieldItem(settings, 10, 13, Items.NETHERITE_INGOT),
        new Item.Settings().maxDamage(2500));


    public static Item registerItem(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
        final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Jonky.MOD_ID, path));
        return Items.register(registryKey, factory, settings);
    }

    public static void registerModItems() {
        Jonky.LOGGER.info("Registering Mod Items for" + Jonky.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(HEAVY_SHIELD);
        });
    }
}
