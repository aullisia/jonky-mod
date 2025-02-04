package jonky.modid.item.custom;

import com.github.crimsondawn45.fabricshieldlib.lib.object.FabricShieldItem;
import jonky.modid.component.ModComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class HeavyShieldItem extends FabricShieldItem {
    public HeavyShieldItem(Settings settings, int coolDownTicks, int enchantability, Item... repairItems) {
        super(settings, coolDownTicks, enchantability, repairItems);
    }

    public void onCraft(ItemStack stack, World world) {
        stack.set(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT, 0);
    }
}
