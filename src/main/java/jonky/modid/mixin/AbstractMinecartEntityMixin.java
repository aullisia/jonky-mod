package jonky.modid.mixin;

import jonky.modid.util.CustomSpeedAccessor;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public class AbstractMinecartEntityMixin implements CustomSpeedAccessor {
    @Unique
    private double customSpeed = -1.0D;

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomSpeed(NbtCompound nbt, CallbackInfo ci) {
        nbt.putDouble("CustomSpeed", this.customSpeed);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomSpeed(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CustomSpeed")) {
            this.customSpeed = nbt.getDouble("CustomSpeed");
        }
    }

    public double getCustomSpeed() {
        return this.customSpeed;
    }

    public void setCustomSpeed(double speed) {
        this.customSpeed = speed;
    }
}
