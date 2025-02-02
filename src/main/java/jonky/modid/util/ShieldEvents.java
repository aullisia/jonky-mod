package jonky.modid.util;

import com.github.crimsondawn45.fabricshieldlib.lib.event.ShieldBlockCallback;
import jonky.modid.Jonky;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ShieldEvents {
    private static ActionResult shieldThorns(LivingEntity defender, DamageSource source, float amount, Hand hand, ItemStack shield) {
        if (defender.isBlocking()) {
            World world = defender.getWorld();
            DynamicRegistryManager registryManager = world.getRegistryManager();
            RegistryEntry<DamageType> damageTypeEntry = registryManager.getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(DamageTypes.THORNS);
            RegistryEntry<Enchantment>  enchantmentEntry = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.THORNS);
            int thornsLevel = EnchantmentHelper.getLevel(enchantmentEntry, shield);
            if (thornsLevel > 0 && source.getAttacker() instanceof LivingEntity attacker && !world.isClient) {
                if (defender.getRandom().nextFloat() < 0.15F * thornsLevel) {
                    // Apply Thorns damage (random 1-4)
                    float thornsDamage = 1.0F + defender.getRandom().nextInt(4);
                    attacker.damage((ServerWorld) world, new DamageSource(damageTypeEntry), thornsDamage);

                    // Knockback effect
                    attacker.takeKnockback(0.5, defender.getX() - attacker.getX(), defender.getZ() - attacker.getZ());

                    // Reduce shield durability
                    EquipmentSlot slot = (hand == Hand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    shield.damage(2, defender, slot);
//                    Jonky.LOGGER.info("{}'s Thorns {} activated, damaging {} for {} HP",
//                            defender.getName().getString(), thornsLevel, attacker.getName().getString(), thornsDamage);
                }
            }
        }
        return ActionResult.PASS;
    }

    public static void registerShieldEvents() {
        ShieldBlockCallback.EVENT.register(ShieldEvents::shieldThorns);
    }
}
