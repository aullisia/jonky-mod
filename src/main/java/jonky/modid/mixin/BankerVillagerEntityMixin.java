package jonky.modid.mixin;

import jonky.modid.item.ModItems;
import jonky.modid.villager.ModVillagers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;

import static jonky.modid.util.BanknoteUtils.createBanknoteStack;

@Mixin(Villager.class)
public class BankerVillagerEntityMixin {
    private static boolean isBanker(Villager villager) {
        return villager.getVillagerData().profession().value().equals(ModVillagers.BANKER);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void onInteractMob(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Villager villager = (Villager) (Object) this;
        if (!player.level().isClientSide()) {
            if (isBanker(villager)) {
                setBankerOffers(villager);
                resetTradePrices(villager, player);
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;
        if (!villager.level().isClientSide()) {
            if (villager.getTradingPlayer() != null && isBanker(villager)) {
                resetTradePrices(villager, villager.getTradingPlayer());
            }
        }
    }

    @Unique
    private static MerchantOffer createTradeOffer(int bankNoteValue, int cost, int banknoteAmount) {
        return new MerchantOffer(
                new ItemCost(Items.DIAMOND, cost),
                createBanknoteStack(bankNoteValue, banknoteAmount),
                100, 0, 0f
        );
    }

    @Unique
    private void setBankerOffers(Villager villager) {
        MerchantOffers list = new MerchantOffers();

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
    private void resetTradePrices(Villager villager, Player player) {
        MerchantOffers offers = villager.getOffers();
        offers.forEach(offer -> {
            offer.resetSpecialPriceDiff();
            offer.resetUses();
        });
    }
}