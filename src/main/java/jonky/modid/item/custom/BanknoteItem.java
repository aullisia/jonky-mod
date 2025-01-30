package jonky.modid.item.custom;

import jonky.modid.Jonky;
import jonky.modid.component.ModComponents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static jonky.modid.util.BanknoteUtils.createBanknoteStack;

public class BanknoteItem extends Item {
    private int count = 0;

    public BanknoteItem(Settings settings) {
        super(settings);
    }

    private void convertBanknote(ItemStack stack, PlayerInventory inventory, boolean isSneaking) {
        int banknoteValue = Objects.requireNonNull(stack.get(ModComponents.BANKNOTE_VALUE_COMPONENT));
        List<Integer> denominations = Arrays.asList(5, 10, 20, 50, 100, 200, 500);

        // Handle Sneaking: Downgrade the banknote value
        if (isSneaking) {
            if (banknoteValue <= 5) {
                //Jonky.LOGGER.warn("Cannot downgrade below the minimum denomination");
                return;
            }

            int currentIndex = denominations.indexOf(banknoteValue);
            if (currentIndex == -1 || currentIndex == 0) return;

            int previousDenom = denominations.get(currentIndex - 1);
            int stackCount = stack.getCount();
            int totalWorth = banknoteValue * stackCount;

            // Process downgrading by breaking into smaller denominations
            while (totalWorth >= previousDenom) {
                int maxNewItems = totalWorth / previousDenom;
                ItemStack newStack = createBanknoteStack(previousDenom, maxNewItems);
                inventory.offer(newStack, true);
                totalWorth -= maxNewItems * previousDenom;
            }

            // If there's leftover worth, keep it in the original stack
            if (totalWorth > 0) {
                int remainingCount = totalWorth / banknoteValue;
                stack.setCount(remainingCount);
            } else {
                stack.setCount(0);
            }

            //Jonky.LOGGER.info("Downgraded stack of {} to smaller denominations.", banknoteValue);
        } else {
            // Handle Normal Conversion: Upgrade the banknote value
            if (banknoteValue >= 500) {
                //Jonky.LOGGER.warn("Cannot upgrade above the maximum denomination");
                return;
            }

            int currentIndex = denominations.indexOf(banknoteValue);
            if (currentIndex == -1 || currentIndex == denominations.size() - 1) return;

            int nextDenom = denominations.get(currentIndex + 1);
            int stackCount = stack.getCount();
            int totalWorth = banknoteValue * stackCount;

            // Process upgrading to the next higher denomination
            int maxNewItems = totalWorth / nextDenom;
            int remainderWorth = totalWorth % nextDenom;

            // Create the upgraded notes
            if (maxNewItems > 0) {
                ItemStack newStack = createBanknoteStack(nextDenom, maxNewItems);
                inventory.offer(newStack, true);
            }

            // Update the current stack with the remainder worth
            if (remainderWorth > 0) {
                int remainingCount = remainderWorth / banknoteValue;
                stack.setCount(remainingCount);
            } else {
                stack.setCount(0);
            }
            //Jonky.LOGGER.info("Upgraded stack of {} to higher denomination {} with {} remaining.", banknoteValue, nextDenom, remainderWorth);
        }
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ConsumableComponent consumableComponent = itemStack.get(DataComponentTypes.CONSUMABLE);
        if (consumableComponent != null) {
            return consumableComponent.consume(user, itemStack, hand);
        } else {
            count++;
            if(count >= 1){
                count = 0;
                user.getItemCooldownManager().set(itemStack, 10);
                convertBanknote(itemStack, user.getInventory(), user.isSneaking());
            }

            EquippableComponent equippableComponent = itemStack.get(DataComponentTypes.EQUIPPABLE);
            return (ActionResult)(equippableComponent != null && equippableComponent.swappable() ? equippableComponent.equip(itemStack, user) : ActionResult.PASS);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Integer value = stack.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
        if (value == null) return;

        String key;
        Formatting format;

        key = switch (value) {
            case 5 -> {
                format = Formatting.GRAY;
                yield "5";
            }
            case 10 -> {
                format = Formatting.RED;
                yield "10";
            }
            case 20 -> {
                format = Formatting.BLUE;
                yield "20";
            }
            case 50 -> {
                format = Formatting.GOLD;
                yield "50";
            }
            case 100 -> {
                format = Formatting.GREEN;
                yield "100";
            }
            case 200 -> {
                format = Formatting.YELLOW;
                yield "200";
            }
            case 500 -> {
                format = Formatting.LIGHT_PURPLE;
                yield "500";
            }
            default -> {
                format = Formatting.DARK_RED;
                yield "";
            }
        };

        if (!key.isEmpty()) {
            tooltip.add(Text.translatable("item.banknote.amount.info." + key).formatted(format));
        } else {
            tooltip.add(Text.translatable("item.banknote.amount.info").formatted(format));
        }
    }
}
