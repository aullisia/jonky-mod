package jonky.modid.mixin;

import jonky.modid.block.ModBlocks;
import jonky.modid.block.custom.CopperRail.CopperRailBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior.class)
public class NewMinecartBehaviorMixin {
    @Inject(method = "calculateBoostTrackSpeed", at = @At("HEAD"), cancellable = true)
    private void copperRailBoost(Vec3 deltaMovement, BlockPos pos, BlockState state, CallbackInfoReturnable<Vec3> cir) {
        if (state.is(ModBlocks.COPPER_RAIL) && state.getValue(CopperRailBlock.POWERED)) {
            if (deltaMovement.length() > 0.01) {
                cir.setReturnValue(deltaMovement.normalize().scale(deltaMovement.length() + 0.06));
            } else {
                Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                Vec3 powerDirection = new Vec3(facing.getStepX(), 0, facing.getStepZ());
                cir.setReturnValue(powerDirection.lengthSqr() <= 0.0 ? deltaMovement : powerDirection.scale(0.2));
            }
        }
    }

    @Inject(method = "calculateHaltTrackSpeed", at = @At("HEAD"), cancellable = true)
    private void copperRailHalt(Vec3 deltaMovement, BlockState state, CallbackInfoReturnable<Vec3> cir) {
        if (state.is(ModBlocks.COPPER_RAIL) && !state.getValue(CopperRailBlock.POWERED)) {
            cir.setReturnValue(deltaMovement.length() < 0.03 ? Vec3.ZERO : deltaMovement.scale(0.5));
        }
    }
}