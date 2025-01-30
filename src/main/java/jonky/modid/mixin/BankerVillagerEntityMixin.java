package jonky.modid.mixin;

import jonky.modid.Jonky;
import jonky.modid.item.ModItems;
import jonky.modid.villager.ModVillagers;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@Mixin(VillagerEntity.class)
public class BankerVillagerEntityMixin{
    @Shadow @Final private static Logger LOGGER;

    private static boolean isBanker (VillagerEntity villager) {
        return villager.getVillagerData().getProfession().equals(ModVillagers.BANKER);
    }

    @Inject(method = "interactMob", at = @At("HEAD"))
    private void onInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        VillagerEntity villager = (VillagerEntity) (Object) this;
        if (!player.getWorld().isClient()) {
            if(isBanker(villager)) {
                setBankerOffers(villager);
                resetTradePrices(villager, player);
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci){
        VillagerEntity villager = (VillagerEntity) (Object) this;
        if (!villager.getWorld().isClient()) {
            if (villager.hasCustomer() && isBanker(villager)){
                resetTradePrices(villager, villager.getCustomer());
            }
        }
    }

    private static ItemStack createBanknoteStack(int customModelData, int banknoteAmount){
        // Creating Banknote
        ItemStack stack = new ItemStack(ModItems.BANKNOTE, banknoteAmount);

        // Setting customModelData
        CustomModelDataComponent customModelDataComponent = new CustomModelDataComponent(customModelData);
        stack.set(DataComponentTypes.CUSTOM_MODEL_DATA, customModelDataComponent);

        return stack;
    }

    private static TradeOffer createTradeOffer(int customModelData, int cost, int banknoteAmount) {
        ItemStack stack = createBanknoteStack(customModelData, banknoteAmount);

        // Creating Trade Offer
        TradeOffer offer = new TradeOffer(
                new TradedItem(Items.DIAMOND, cost),
                stack,
                100, 0, 0f
        );

        return offer;
    }

    private void setBankerOffers(VillagerEntity villager) {
        TradeOfferList list = new TradeOfferList();


        Collections.addAll(
                list,
                createTradeOffer(2, 1, 2),
                createTradeOffer(3, 1, 1),
                createTradeOffer(4, 2, 1),
                createTradeOffer(5, 5, 1),
                createTradeOffer(6, 10, 1),
                createTradeOffer(7, 20, 1),
                createTradeOffer(8, 50, 1)
        );

        villager.setOffers(list);
    }

    private void resetTradePrices(VillagerEntity villager, PlayerEntity player) {
        TradeOfferList offers = villager.getOffers();
        offers.forEach(offer -> {
            offer.clearSpecialPrice();
            offer.resetUses();
        });
    }
}
