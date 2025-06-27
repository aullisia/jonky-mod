package jonky.modid.item.custom;

import jonky.modid.Jonky;
import jonky.modid.network.ModNetwork;
import jonky.modid.util.CustomSpeedAccessor;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class WrenchItem extends Item {
    public WrenchItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            itemStack = itemStack.copy();
            user.getStackInHand(hand).decrement(1);

            user.giveItemStack(new ItemStack(Items.GOLD_INGOT));
            user.giveItemStack(new ItemStack(Items.STICK));
            user.giveItemStack(new ItemStack(Items.LEATHER));
        }

        return ActionResult.SUCCESS;
    }
}
