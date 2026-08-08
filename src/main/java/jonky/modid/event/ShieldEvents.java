package jonky.modid.event;

import jonky.modid.component.ModComponents;
import jonky.modid.item.ModItems;
import jonky.modid.util.EnchantmentUtils;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

public class ShieldEvents {
    public interface ShieldBlockCallback {
        InteractionResult shieldBlocked(LivingEntity defender, DamageSource source, float amount, InteractionHand hand, ItemStack shield);
    }

    public interface ShieldDisabledCallback {
        InteractionResult shieldDisabled(Player defender, InteractionHand hand, ItemStack shield);
    }

    public static final Event<ShieldBlockCallback> SHIELD_BLOCKED = EventFactory.createArrayBacked(ShieldBlockCallback.class,
            callbacks -> (defender, source, amount, hand, shield) -> {
                for (ShieldBlockCallback callback : callbacks) {
                    InteractionResult result = callback.shieldBlocked(defender, source, amount, hand, shield);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            });

    public static final Event<ShieldDisabledCallback> SHIELD_DISABLED = EventFactory.createArrayBacked(ShieldDisabledCallback.class,
            callbacks -> (defender, hand, shield) -> {
                for (ShieldDisabledCallback callback : callbacks) {
                    InteractionResult result = callback.shieldDisabled(defender, hand, shield);
                    if (result.consumesAction()) {
                        return result;
                    }
                }
                return InteractionResult.PASS;
            });

    public static void onShieldBlocked(LivingEntity defender, DamageSource source, float amount, InteractionHand hand, ItemStack shield) {
        SHIELD_BLOCKED.invoker().shieldBlocked(defender, source, amount, hand, shield);
    }

    public static void onShieldDisabled(Player defender, InteractionHand hand, ItemStack shield) {
        SHIELD_DISABLED.invoker().shieldDisabled(defender, hand, shield);
    }

    // Enchantments
    private static InteractionResult shieldKnockback(LivingEntity defender, DamageSource source, float amount, InteractionHand hand, ItemStack shield) {
        if (defender.isBlocking()) {
            RegistryAccess registryAccess = defender.level().registryAccess();
            Holder<Enchantment> enchantmentEntry = EnchantmentUtils.getEnchantmentEntry(Enchantments.KNOCKBACK, registryAccess);
            int knockbackLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantmentEntry, shield);

            if (knockbackLevel > 0 && source.getEntity() instanceof LivingEntity attacker && !defender.level().isClientSide()) {
                if (attacker.getType() == EntityTypes.WARDEN) {
                    return InteractionResult.PASS;
                }

                // Resistance for Squid
                double resistance = attacker.getType() == EntityTypes.SQUID ? 0.5 : 1.0;

                // Determine base strength
                double baseStrength = defender.isSprinting() ? 0.6 : 0.5;

                // Apply enchantment multiplier
                double multiplier = 1.0 + (knockbackLevel == 1 ? 1.05 : knockbackLevel >= 2 ? 1.90 : 0.0);
                double strength = baseStrength * multiplier;

                // Apply resistance
                strength *= resistance;

                // Movement-based reduction
                Vec3 attackerMovement = attacker.getDeltaMovement();
                Vec3 lookVec = attacker.getLookAngle();
                if (!attackerMovement.equals(Vec3.ZERO)) {
                    Vec3 normalizedMovement = attackerMovement.normalize();
                    Vec3 normalizedLook = lookVec.normalize();
                    if (normalizedMovement.dot(normalizedLook) > 0.5) {
                        strength *= 0.6;
                    }
                }

                // Airborne reduction for players
                if (attacker instanceof Player && !attacker.onGround()) {
                    strength *= 0.5;
                }

                // Cap for players
                if (knockbackLevel > 2 && attacker instanceof Player) {
                    strength = Math.min(strength, 3.0);
                } else {
                    // Normal cap at 6 blocks (assuming 1.0 strength ≈ 1 block)
                    strength = Math.min(strength, 6.0);
                }

                // Apply knockback
                attacker.knockback(strength, defender.getX() - attacker.getX(), defender.getZ() - attacker.getZ(), null, 0.0F);
            }
        }

        return InteractionResult.PASS;
    }

    // Heavy shield
    private static InteractionResult heavyShieldEnergy(LivingEntity defender, DamageSource source, float amount, InteractionHand hand, ItemStack shield) {
        if (defender.isBlocking()) {
            if (!shield.is(ModItems.HEAVY_SHIELD)) return InteractionResult.PASS;
            // Setting last attacker
            Entity attacker = source.getEntity();
            if (attacker == null) return InteractionResult.PASS;
            shield.set(ModComponents.LAST_ATTACKER_COMPONENT, attacker.getId());

            // Shield energy
            Integer shieldEnergy = shield.get(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT);
            if (shieldEnergy == null) { shieldEnergy = 0; }
            shield.set(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT, shieldEnergy + 1);
        }

        return InteractionResult.PASS;
    }

    private static InteractionResult heavyShieldSurge(Player defender, InteractionHand hand, ItemStack shield) {
        if (!shield.is(ModItems.HEAVY_SHIELD) || defender.level().isClientSide()) return InteractionResult.PASS;

        ServerLevel serverLevel = (ServerLevel) defender.level();

        RegistryAccess registryAccess = defender.level().registryAccess();
        Registry<DamageType> damageTypeRegistry = registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE);
        Holder<DamageType> damageTypeEntry = damageTypeRegistry.wrapAsHolder(damageTypeRegistry.getValue(DamageTypes.THORNS));

        Integer attackerId = shield.get(ModComponents.LAST_ATTACKER_COMPONENT);
        if (attackerId == null) return InteractionResult.PASS;
        Entity attacker = serverLevel.getEntity(attackerId);
        if (attacker == null) return InteractionResult.PASS;

        Integer shieldEnergy = shield.get(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT);
        shield.set(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT, 0);
        if (shieldEnergy == null) { shieldEnergy = 0; }
        float damageAmount = (float) (3 * (Math.pow(1.5, shieldEnergy) - 1));
        defender.playSound(SoundEvents.MACE_SMASH_GROUND_HEAVY, 2.0F, 0.7F);
        attacker.hurtServer(serverLevel, new DamageSource(damageTypeEntry), damageAmount);

        return InteractionResult.PASS;
    }

    public static void registerShieldEvents() {
        SHIELD_BLOCKED.register(ShieldEvents::shieldKnockback);
        SHIELD_DISABLED.register(ShieldEvents::heavyShieldSurge);
        SHIELD_BLOCKED.register(ShieldEvents::heavyShieldEnergy);
    }
}