package jonky.modid.mixin;

import jonky.modid.Jonky;
import jonky.modid.util.CustomSpeedAccessor;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ExperimentalMinecartController.class)
public class ExperimentalMinecartControllerMixin  {
    @Overwrite
    public double getMaxSpeed(ServerWorld world) {
        AbstractMinecartEntity minecart = ((MinecartControllerAccessor) this).getMinecart();
        double customSpeed = ((CustomSpeedAccessor) minecart).getCustomSpeed();
        //Jonky.LOGGER.warn("Custom speed: " + customSpeed);
        if (customSpeed > 0) {
            return customSpeed * (minecart.isTouchingWater() ? (double)0.5F : (double)1.0F) / (double)20.0F;
        } else {
            return (double)world.getGameRules().getInt(GameRules.MINECART_MAX_SPEED) * (minecart.isTouchingWater() ? (double)0.5F : (double)1.0F) / (double)20.0F;
        }
    }
}
