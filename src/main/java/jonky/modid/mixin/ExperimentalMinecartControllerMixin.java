package jonky.modid.mixin;


import jonky.modid.block.ModBlocks;
import jonky.modid.block.custom.CopperRail.CopperRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperimentalMinecartController.class)
public class ExperimentalMinecartControllerMixin {
    private AbstractMinecartEntity getMinecart() {
        return ((MinecartControllerAccessor)(Object)this).getMinecart();
    }

    private Vec3d getRailDirection(BlockState railState) {
        Direction facing = railState.get(Properties.HORIZONTAL_FACING);
        return switch (facing) {
            case NORTH -> new Vec3d(0, 0, -1);
            case SOUTH -> new Vec3d(0, 0, 1);
            case WEST -> new Vec3d(-1, 0, 0);
            case EAST -> new Vec3d(1, 0, 0);
            default -> Vec3d.ZERO;
        };
    }


    @Inject(method="accelerateFromPoweredRail", at = @At("HEAD"), cancellable = true)
    private void accelerateFromPoweredRail(Vec3d velocity, BlockPos railPos, BlockState railState, CallbackInfoReturnable<Vec3d> cir) {
        if (railState.isOf(ModBlocks.COPPER_RAIL) && railState.get(CopperRailBlock.POWERED)) {
            Vec3d railDirection = getRailDirection(railState).normalize();
            double accelerationAmount = 0.06;

            Vec3d newVelocity = velocity.add(railDirection.multiply(accelerationAmount));

            cir.setReturnValue(newVelocity);
        }
    }

    @Inject(method="decelerateFromPoweredRail", at = @At("HEAD"), cancellable = true)
    private void decelerateFromPoweredRail(Vec3d velocity, BlockState railState, CallbackInfoReturnable<Vec3d> cir) {
        if (railState.isOf(ModBlocks.COPPER_RAIL) && !railState.get(CopperRailBlock.POWERED)) {
            double speed = velocity.length();

            if (speed < 0.03) {
                Vec3d pos = getMinecart().getPos();
                BlockPos blockPos = BlockPos.ofFloored(pos);
                Vec3d center = new Vec3d(blockPos.getX() + 0.5, pos.y, blockPos.getZ() + 0.5);
                Vec3d offset = center.subtract(pos).multiply(0.3);

                if (offset.lengthSquared() < 0.0004) {
                    cir.setReturnValue(Vec3d.ZERO);
                } else {
                    cir.setReturnValue(offset);
                }
            } else {
                cir.setReturnValue(velocity.multiply(0.1));
            }
        }
    }
}
