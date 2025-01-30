package jonky.modid.enchantment.custom;

import com.mojang.serialization.MapCodec;
import jonky.modid.Jonky;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class ForsakingEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<ForsakingEnchantmentEffect> CODEC = MapCodec.unit(ForsakingEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        Jonky.LOGGER.warn("APPLY");
        if (target instanceof ProjectileEntity projectile) {
            // Logic using enchantmentLevelBasedValue and blockState
            Jonky.LOGGER.warn("Projectile");
            // Step 2: Spawn a temporary Armor Stand (or other entity) as the new owner
            ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
            armorStand.setInvisible(true); // Make the armor stand invisible
            armorStand.setInvulnerable(true); // Prevent it from taking damage
            armorStand.setNoGravity(true); // Make sure it doesn't fall

            armorStand.updatePosition(projectile.getX(), projectile.getY(), projectile.getZ());

            world.spawnEntity(armorStand);

            projectile.setOwner(armorStand);

            armorStand.discard();
        } else {
            // Log or handle the case where the target is not a projectile
            Jonky.LOGGER.warn("Target is not a projectile entity");
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
