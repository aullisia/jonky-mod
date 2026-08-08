package jonky.modid.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class WrenchItem extends Item {
    public WrenchItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);

        if (!world.isClientSide()) {
            itemStack = itemStack.copy();
            user.getItemInHand(hand).shrink(1);

            user.getInventory().add(new ItemStack(Items.GOLD_INGOT));
            user.getInventory().add(new ItemStack(Items.STICK));
            user.getInventory().add(new ItemStack(Items.LEATHER));
        }

        return InteractionResult.SUCCESS;
    }
}