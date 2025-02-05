package jonky.modid.util;

import jonky.modid.component.ModComponents;
import jonky.modid.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanknoteUtils {
    public static final Map<Integer, ItemStack> ATMItemList = new HashMap<>();
    static {
        ATMItemList.put(1, BanknoteUtils.createBanknoteStack(5, 1));
        ATMItemList.put(2, BanknoteUtils.createBanknoteStack(10, 1));
        ATMItemList.put(3, BanknoteUtils.createBanknoteStack(20, 1));
        ATMItemList.put(4, BanknoteUtils.createBanknoteStack(50, 1));
        ATMItemList.put(5, BanknoteUtils.createBanknoteStack(100, 1));
        ATMItemList.put(6, BanknoteUtils.createBanknoteStack(200, 1));
        ATMItemList.put(7, BanknoteUtils.createBanknoteStack(500, 1));
    }

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
