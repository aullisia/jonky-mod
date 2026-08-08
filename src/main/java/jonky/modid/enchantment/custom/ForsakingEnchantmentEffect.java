package jonky.modid.enchantment.custom;

import com.mojang.serialization.MapCodec;
import jonky.modid.Jonky;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public class ForsakingEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<ForsakingEnchantmentEffect> CODEC = MapCodec.unit(ForsakingEnchantmentEffect::new);

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        Jonky.LOGGER.warn("APPLY");
        if (target instanceof Projectile projectile) {
            // Logic using enchantmentLevelBasedValue and blockState
            Jonky.LOGGER.warn("Projectile");
            // Step 2: Spawn a temporary Armor Stand (or other entity) as the new owner
            ArmorStand armorStand = new ArmorStand(world, projectile.getX(), projectile.getY(), projectile.getZ());
            armorStand.setInvisible(true); // Make the armor stand invisible
            armorStand.setInvulnerable(true); // Prevent it from taking damage
            armorStand.setNoGravity(true); // Make sure it doesn't fall

            world.addFreshEntity(armorStand);

            projectile.setOwner(armorStand);

            armorStand.discard();
        } else {
            // Log or handle the case where the target is not a projectile
            Jonky.LOGGER.warn("Target is not a projectile entity");
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}