package jonky.modid.item;

import jonky.modid.Jonky;
import jonky.modid.item.custom.BanknoteItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
//    public static final Item BANKNOTE = registerItem("banknote", new Item(new Item.Settings()));
    public static final Item BANKNOTE = registerItem("banknote", new BanknoteItem(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Jonky.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Jonky.LOGGER.info("Registering Mod Items for" + Jonky.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
//            entries.add(BANKNOTE);
        });
    }
}
