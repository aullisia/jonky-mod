package jonky.modid.util;

import jonky.modid.item.ModItems;
import jonky.modid.villager.ModVillagers;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;

public class ModCustomTrades {
    // Helper function for creating banknote ItemStacks
    public static ItemStack createBanknote(int customModelData, int itemCount) {
        ItemStack stack = new ItemStack(ModItems.BANKNOTE, itemCount);

        // Setting customModelData
        CustomModelDataComponent customModelDataComponent = new CustomModelDataComponent(customModelData);
        stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, customModelDataComponent);

        return stack;
    }

    public static void registerCustomTrades() {
        TradeOfferHelper.registerVillagerOffers(ModVillagers.BANKER, 1, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(Items.DIAMOND, 5),
                    createBanknote(2,7),
                    6, 5, 0.05f
            ));
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(Items.DIAMOND, 5),
                    createBanknote(3,1),
                    6, 5, 0.05f
            ));
        });
    }
}
