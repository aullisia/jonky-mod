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

        if (isSneaking) {
            if (banknoteValue <= 5) {
                Jonky.LOGGER.warn("Minimum Value");
                return;
            }

            int currentIndex = denominations.indexOf(banknoteValue);
            if (currentIndex == -1 || currentIndex == 0) return;

            int previousDenom = denominations.get(currentIndex - 1);
            int quotient = banknoteValue / previousDenom;
            int remainderValue = banknoteValue % previousDenom;

            int stackCount = stack.getCount();
            int totalPrevious = quotient * stackCount;
            int totalRemainder = (remainderValue != 0) ? stackCount : 0;

            // Remove the original stack
            int slot = inventory.getSlotWithStack(stack);
            if (slot != -1) inventory.removeStack(slot);

            // Add previous denomination stacks
            if (totalPrevious > 0) {
                ItemStack previousStack = createBanknoteStack(previousDenom, totalPrevious);
                inventory.offer(previousStack, true);
            }

            // Add remainder stack if applicable
            if (totalRemainder > 0 && remainderValue != 0) {
                ItemStack remainderStack = createBanknoteStack(remainderValue, totalRemainder);
                inventory.offer(remainderStack, true);
            }
        } else {
            if (banknoteValue >= 500) {
                Jonky.LOGGER.warn("Maximum Value");
                return;
            }

            int currentIndex = denominations.indexOf(banknoteValue);
            if (currentIndex == -1 || currentIndex == denominations.size() - 1) return;

            int nextDenom = denominations.get(currentIndex + 1);
            int requiredCurrent = (int) Math.ceil((double) nextDenom / banknoteValue);

            // Calculate total current notes in inventory
            int totalCurrent = 0;
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack invStack = inventory.getStack(i);
                if (invStack.getItem() == stack.getItem()) {
                    Integer value = invStack.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
                    if (value != null && value == banknoteValue) {
                        totalCurrent += invStack.getCount();
                    }
                }
            }

            if (totalCurrent < requiredCurrent) {
                Jonky.LOGGER.warn("Not enough notes to combine");
                return;
            }

            // Consume required current notes
            int remainingToConsume = requiredCurrent;
            for (int i = 0; i < inventory.size(); i++) {
                if (remainingToConsume <= 0) break;
                ItemStack invStack = inventory.getStack(i);
                if (invStack.getItem() == stack.getItem()) {
                    Integer value = invStack.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
                    if (value != null && value == banknoteValue) {
                        int consume = Math.min(remainingToConsume, invStack.getCount());
                        invStack.decrement(consume);
                        if (invStack.isEmpty()) {
                            inventory.setStack(i, ItemStack.EMPTY);
                        }
                        remainingToConsume -= consume;
                    }
                }
            }

            // Create next denomination and remainder
            ItemStack nextDenomStack = createBanknoteStack(nextDenom, 1);
            inventory.offer(nextDenomStack, true);

            int remainderValue = (banknoteValue * requiredCurrent) - nextDenom;
            if (remainderValue > 0) {
                ItemStack remainderStack = createBanknoteStack(remainderValue, 1);
                inventory.offer(remainderStack, true);
            }
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
