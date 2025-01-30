package jonky.modid.util;

import jonky.modid.component.ModComponents;
import jonky.modid.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class BanknoteUtils {
    public static ItemStack createBanknoteStack(int banknoteValue, int banknoteAmount){
        // Creating Banknote
        ItemStack stack = new ItemStack(ModItems.BANKNOTE, banknoteAmount);

        stack.set(ModComponents.BANKNOTE_VALUE_COMPONENT, banknoteValue);

        // Honestly I don't know why but all these lists are required...
        List<Float> floatList = Collections.emptyList();
        List<Boolean> booleanList = Collections.emptyList();
        List<String> stringList = List.of(String.valueOf(banknoteValue));
        List<Integer> integerList = Collections.emptyList();

        // Setting customModelData
        CustomModelDataComponent customModelDataComponent = new CustomModelDataComponent(floatList, booleanList, stringList, integerList);
        stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, customModelDataComponent);

        return stack;
    }
}
