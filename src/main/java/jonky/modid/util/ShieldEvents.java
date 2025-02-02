package jonky.modid.util;

import com.github.crimsondawn45.fabricshieldlib.lib.event.ShieldBlockCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ShieldEvents {
    private static ActionResult shieldThorns(LivingEntity defender, DamageSource source, float amount, Hand hand, ItemStack shield) {
        if (defender.isBlocking()) {
            World world = defender.getWorld();
            DynamicRegistryManager registryManager = world.getRegistryManager();
            RegistryEntry<DamageType> damageTypeEntry = registryManager.getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(DamageTypes.THORNS);
            RegistryEntry<Enchantment>  enchantmentEntry = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.THORNS);
            RegistryEntry<Enchantment>  enchantmentEntryKnockback = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK);
            int thornsLevel = EnchantmentHelper.getLevel(enchantmentEntry, shield);
            if (thornsLevel > 0 && source.getAttacker() instanceof LivingEntity attacker && !world.isClient) {
                if (defender.getRandom().nextFloat() < 0.15F * thornsLevel) {
                    // Apply Thorns damage (random 1-4)
                    float thornsDamage = 1.0F + defender.getRandom().nextInt(4);
                    attacker.damage((ServerWorld) world, new DamageSource(damageTypeEntry), thornsDamage);

                    // Knockback effect
                    if(EnchantmentHelper.getLevel(enchantmentEntryKnockback, shield) <= 0)
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

    private static ActionResult shieldKnockback(LivingEntity defender, DamageSource source, float amount, Hand hand, ItemStack shield) {
        if (defender.isBlocking()) {
            World world = defender.getWorld();
            DynamicRegistryManager registryManager = world.getRegistryManager();
            RegistryEntry<Enchantment>  enchantmentEntry = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK);
            int knockbackLevel = EnchantmentHelper.getLevel(enchantmentEntry, shield);
            if (knockbackLevel > 0 && source.getAttacker() instanceof LivingEntity attacker && !world.isClient) {
                if (attacker.getType() == EntityType.WARDEN) {
                    return ActionResult.PASS;
                }

                // Resistance for Squid
                double resistance = attacker.getType() == EntityType.SQUID ? 0.5 : 1.0;

                // Determine base strength
                double baseStrength = defender.isSprinting() ? 0.6 : 0.5;

                // Apply enchantment multiplier
                double multiplier = 1.0 + (knockbackLevel == 1 ? 1.05 : knockbackLevel >= 2 ? 1.90 : 0.0);
                double strength = baseStrength * multiplier;

                // Apply resistance
                strength *= resistance;

                // Movement-based reduction
                Vec3d attackerMovement = attacker.getVelocity();
                Vec3d lookVec = attacker.getRotationVec(1.0F);
                if (!attackerMovement.equals(Vec3d.ZERO)) {
                    Vec3d normalizedMovement = attackerMovement.normalize();
                    Vec3d normalizedLook = lookVec.normalize();
                    if (normalizedMovement.dotProduct(normalizedLook) > 0.5) {
                        strength *= 0.6;
                    }
                }

                // Airborne reduction for players
                if (attacker instanceof PlayerEntity && !attacker.isOnGround()) {
                    strength *= 0.5;
                }

                // Command-based cap for players
                if (knockbackLevel > 2 && attacker instanceof PlayerEntity) {
                    strength = Math.min(strength, 3.0);
                } else {
                    // Normal cap at 6 blocks (assuming 1.0 strength ≈ 1 block)
                    strength = Math.min(strength, 6.0);
                }

                // Apply knockback
                attacker.takeKnockback(strength, defender.getX() - attacker.getX(), defender.getZ() - attacker.getZ());

            }
        }
        return ActionResult.PASS;
    }

    public static void registerShieldEvents() {
        ShieldBlockCallback.EVENT.register(ShieldEvents::shieldThorns);
        ShieldBlockCallback.EVENT.register(ShieldEvents::shieldKnockback);
    }
}
