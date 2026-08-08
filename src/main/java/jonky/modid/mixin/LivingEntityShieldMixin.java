package jonky.modid.mixin;

import jonky.modid.event.ShieldEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityShieldMixin {
    @Inject(method = "blockUsingItem", at = @At("HEAD"))
    private void displayInjectionShield(ServerLevel level, LivingEntity source, DamageSource damageSource, float damage, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        ItemStack blockingItem = livingEntity.getItemBlockingWith();
        if (blockingItem.isEmpty()) return;

        if ((Object) this instanceof Player player && source.getSecondsToDisableBlocking() > 0.0F) {
            ShieldEvents.onShieldDisabled(player, livingEntity.getUsedItemHand(), blockingItem);
        }

        ShieldEvents.onShieldBlocked(livingEntity, damageSource, damage, livingEntity.getUsedItemHand(), blockingItem);
    }
}