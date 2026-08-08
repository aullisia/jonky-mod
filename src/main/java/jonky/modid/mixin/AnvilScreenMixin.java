package jonky.modid.mixin;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Removes anvil cap
@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @ModifyConstant(method = "extractLabels", constant = @Constant(intValue = 40))
    private int mixinAnvilCost(int value) {
        return Integer.MAX_VALUE;
    }
}