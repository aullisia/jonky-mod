package jonky.modid.mixin;

import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Removes anvil cap
@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 40))
    private int mixinAnvilCost1(int value) {
        return Integer.MAX_VALUE;
    }

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 39))
    private int mixinAnvilCost2(int value) {
        return Integer.MAX_VALUE - 1;
    }
}
