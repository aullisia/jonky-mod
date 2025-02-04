package jonky.modid.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public class EnchantmentUtils {
    public static RegistryEntry<Enchantment> getEnchantmentEntry(RegistryKey<Enchantment> enchantment, DynamicRegistryManager registryManager) {
        return registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantment);
    }
}
