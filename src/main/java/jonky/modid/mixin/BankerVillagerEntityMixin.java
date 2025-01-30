package jonky.modid.mixin;

import jonky.modid.component.ModComponents;
import jonky.modid.item.ModItems;
import jonky.modid.villager.ModVillagers;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

import static jonky.modid.util.BanknoteUtils.createBanknoteStack;

@Mixin(VillagerEntity.class)
public class BankerVillagerEntityMixin {
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

    @Unique
    private static TradeOffer createTradeOffer(int bankNoteValue, int cost, int banknoteAmount) {

        // Creating Trade Offer
        TradeOffer offer = new TradeOffer(
                new TradedItem(Items.DIAMOND, cost),
                createBanknoteStack(bankNoteValue, banknoteAmount),
                100, 0, 0f
        );

        return offer;
    }

    @Unique
    private void setBankerOffers(VillagerEntity villager) {
        TradeOfferList list = new TradeOfferList();

        Collections.addAll(
                list,
                createTradeOffer(5, 1, 2),
                createTradeOffer(10, 1, 1),
                createTradeOffer(20, 2, 1),
                createTradeOffer(50, 5, 1),
                createTradeOffer(100, 10, 1),
                createTradeOffer(200, 20, 1),
                createTradeOffer(500, 50, 1)
        );

        villager.setOffers(list);
    }

    @Unique
    private void resetTradePrices(VillagerEntity villager, PlayerEntity player) {
        TradeOfferList offers = villager.getOffers();
        offers.forEach(offer -> {
            offer.clearSpecialPrice();
            offer.resetUses();
        });
    }
}
