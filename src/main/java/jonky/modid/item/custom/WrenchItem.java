package jonky.modid.item.custom;

import jonky.modid.Jonky;
import jonky.modid.network.ModNetwork;
import jonky.modid.util.CustomSpeedAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class WrenchItem extends Item {
    public WrenchItem(Settings settings) {
        super(settings);
    }

    private boolean modifying = false;
    private MinecartEntity currentCart = null;

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (user.isInSneakingPose()) {
            HitResult hit = ProjectileUtil.getCollision(user,
                    entity -> entity instanceof MinecartEntity,
                    5.0D
            );

            if (hit.getType() == HitResult.Type.ENTITY) {
                MinecartEntity cart = (MinecartEntity) ((EntityHitResult) hit).getEntity();
                this.modifying = true;
                this.currentCart = cart;

                user.setCurrentHand(hand);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (modifying && user instanceof PlayerEntity player && currentCart != null) {
            double scroll = ModNetwork.WRENCH_SCROLL_VALUES.getOrDefault(player.getUuid(), 0.0);
            ModNetwork.WRENCH_SCROLL_VALUES.remove(player.getUuid());
            double currentSpeed = ((CustomSpeedAccessor) currentCart).getCustomSpeed();
            double newSpeed = Math.max(-1, currentSpeed + scroll * 0.5);
            ((CustomSpeedAccessor) currentCart).setCustomSpeed(newSpeed);
//            Jonky.LOGGER.warn("New Speed: {}", newSpeed);

            if (!world.isClient) {
                if (newSpeed > 0) {
                    player.sendMessage(
                            Text.literal("Maximum Speed: " + newSpeed),
                            true
                    );
                } else {
                    player.sendMessage(
                            Text.literal("Maximum Speed: Default"),
                            true
                    );
                }
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            this.modifying = false;
            this.currentCart = null;
            ModNetwork.WRENCH_SCROLL_VALUES.remove(user.getUuid()); // Cleanup
        }
        return true;
    }
}
