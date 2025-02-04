package jonky.modid.util;

import com.github.crimsondawn45.fabricshieldlib.lib.event.ShieldBlockCallback;
import com.github.crimsondawn45.fabricshieldlib.lib.event.ShieldDisabledCallback;
import com.mojang.serialization.Codec;
import jonky.modid.Jonky;
import jonky.modid.component.ModComponents;
import jonky.modid.item.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
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
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.UUID;

public class ShieldEvents {
    // Enchantments
    private static ActionResult shieldThorns(LivingEntity defender, DamageSource source, float amount, Hand hand, ItemStack shield) {
        if (defender.isBlocking()) {
            World world = defender.getWorld();
            ServerWorld serverWorld = Objects.requireNonNull(defender.getServer()).getWorld(world.getRegistryKey());
            DynamicRegistryManager registryManager = world.getRegistryManager();
            RegistryEntry<DamageType> damageTypeEntry = registryManager.getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(DamageTypes.THORNS);
            RegistryEntry<Enchantment>  enchantmentEntry = EnchantmentUtils.getEnchantmentEntry(Enchantments.THORNS, registryManager);
            RegistryEntry<Enchantment>  enchantmentEntryKnockback = EnchantmentUtils.getEnchantmentEntry(Enchantments.THORNS, registryManager);

            int thornsLevel = EnchantmentHelper.getLevel(enchantmentEntry, shield);
            if (thornsLevel > 0 && source.getAttacker() instanceof LivingEntity attacker && !world.isClient) {
                if (defender.getRandom().nextFloat() < 0.15F * thornsLevel) {
                    // Apply Thorns damage (random 1-4)
                    float thornsDamage = 1.0F + defender.getRandom().nextInt(4);
                    attacker.damage(serverWorld, new DamageSource(damageTypeEntry), thornsDamage);

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
            RegistryEntry<Enchantment>  enchantmentEntry = EnchantmentUtils.getEnchantmentEntry(Enchantments.KNOCKBACK, registryManager);
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

    // Heavy shield
    private static ActionResult heavyShieldEnergy(LivingEntity defender, DamageSource source, float amount, Hand hand, ItemStack shield) {
        if (defender.isBlocking()) {
            // Setting last attacker
            Entity attacker = source.getAttacker();
            assert attacker != null;
            shield.set(ModComponents.LAST_ATTACKER_COMPONENT, attacker.getId());

            // Shield energy
            Integer shieldEnergy = shield.get(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT);
            if(shieldEnergy == null) {shieldEnergy = 0; }
            shield.set(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT, ++shieldEnergy);
            Jonky.LOGGER.warn(String.valueOf(++shieldEnergy));
        }

        return ActionResult.PASS;
    }

    private static ActionResult heavyShieldSurge(PlayerEntity defender, Hand hand, ItemStack shield) {
        World world = Objects.requireNonNull(defender.getWorld());
        ServerWorld serverWorld = Objects.requireNonNull(defender.getServer()).getWorld(world.getRegistryKey());

        if(shield.getItem() == ModItems.HEAVY_SHIELD && !world.isClient) {
            DynamicRegistryManager registryManager = world.getRegistryManager();
            RegistryEntry<DamageType> damageTypeEntry = registryManager.getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(DamageTypes.THORNS);

            assert serverWorld != null;
            Integer attackerId = shield.get(ModComponents.LAST_ATTACKER_COMPONENT);
            if(attackerId == null) return ActionResult.PASS;
            Entity attacker = serverWorld.getEntityById(attackerId);
            assert attacker != null;

            Integer shieldEnergy = shield.get(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT);
            shield.set(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT, 0);
            if(shieldEnergy == null) {shieldEnergy = 0; }
            float damageAmount = (float) (3 * (Math.pow(1.5, shieldEnergy) - 1));
            defender.playSound(SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, 2f, 0.7f); // Sound doesnt play?
            attacker.damage(serverWorld, new DamageSource(damageTypeEntry), damageAmount);
        }

        return ActionResult.PASS;
    }

    public static void registerShieldEvents() {
        ShieldBlockCallback.EVENT.register(ShieldEvents::shieldThorns);
        ShieldBlockCallback.EVENT.register(ShieldEvents::shieldKnockback);
        ShieldDisabledCallback.EVENT.register(ShieldEvents::heavyShieldSurge);
        ShieldBlockCallback.EVENT.register(ShieldEvents::heavyShieldEnergy);
    }
}
