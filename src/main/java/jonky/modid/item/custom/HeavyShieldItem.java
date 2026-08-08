package jonky.modid.item.custom;

import jonky.modid.component.ModComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class HeavyShieldItem extends ShieldItem {
    public HeavyShieldItem(Properties properties) {
        super(properties);
    }

    public int getShieldEnergy(ItemStack stack) {
        Integer energy = stack.get(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT);
        return energy == null ? 0 : energy;
    }

    public void setShieldEnergy(ItemStack stack, int energy) {
        stack.set(ModComponents.HEAVY_SHIELD_ENERGY_COMPONENT, energy);
    }
}