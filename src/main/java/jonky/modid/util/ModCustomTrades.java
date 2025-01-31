package jonky.modid.util;

import jonky.modid.enchantment.ModEnchantments;
import jonky.modid.villager.ModVillagers;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;

import java.util.Random;

public class ModCustomTrades {
    private static  ItemStack createEnchantedBookStack(RegistryKey<Enchantment> enchantment, int level, Entity entity) {
        ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
        World world = entity.getWorld();
        DynamicRegistryManager registryManager = world.getRegistryManager();
        RegistryEntry<Enchantment> enchantmentEntry = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantment);
        stack.addEnchantment(enchantmentEntry, level);
        return stack;
    }

    public static void registerCustomTrades() {
        Random rand = new Random();

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.LIBRARIAN, 1, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(Items.DIAMOND, rand.nextInt(11)+10),
                    createEnchantedBookStack(ModEnchantments.FORSAKING ,1, entity),
                    12, 1, 0.02f
            ));
        });
    }
}
