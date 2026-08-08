package jonky.modid.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentUtils {
    public static Holder<Enchantment> getEnchantmentEntry(ResourceKey<Enchantment> enchantment, RegistryAccess registryAccess) {
        Registry<Enchantment> registry = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
        return registry.wrapAsHolder(registry.getValue(enchantment));
    }
}