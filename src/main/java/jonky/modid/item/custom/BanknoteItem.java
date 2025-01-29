package jonky.modid.item.custom;

import jonky.modid.Jonky;
import jonky.modid.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BanknoteItem extends Item {
    private int count = 0;

    public BanknoteItem(Settings settings) {
        super(settings);
    }

    private void convertBanknote(ItemStack stack, PlayerInventory inventory, boolean isSneaking) {
        int modelDataValue = Objects.requireNonNull(stack.get(DataComponentTypes.CUSTOM_MODEL_DATA)).value();

        Map<Integer, Integer> worthMap = Map.of(
                2, 5,
                3, 10,
                4, 20,
                5, 50,
                6, 100,
                7, 200,
                8, 500
        );

        int currentWorth = worthMap.getOrDefault(modelDataValue, 0);
        if (currentWorth == 0) {
            Jonky.LOGGER.warn("Cannot convert stack with model data {}", modelDataValue);
            return; // No conversion possible
        }

        if (isSneaking) {
            // Sneaking: Convert higher denominations to lower denominations
            if (modelDataValue <= 2) {
                Jonky.LOGGER.warn("Cannot convert stack {} to a lower denomination.", modelDataValue);
                return; // No lower denomination available
            }

            int itemCount = stack.getCount();
            int totalWorth = currentWorth * itemCount;

            while (totalWorth > 0) {
                boolean converted = false;

                // Iterate from lower denominations to higher ones to distribute the value
                for (int lowerModelData = modelDataValue - 1; lowerModelData >= 2; lowerModelData--) {
                    int lowerWorth = worthMap.get(lowerModelData);
                    int maxNewItems = totalWorth / lowerWorth;

                    if (maxNewItems > 0) {
                        // Add new stack(s) with lower denomination
                        ItemStack newStack = new ItemStack(stack.getItem(), maxNewItems);
                        CustomModelDataComponent newCustomModelData = new CustomModelDataComponent(lowerModelData);
                        newStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, newCustomModelData);
                        inventory.insertStack(newStack);

                        totalWorth -= maxNewItems * lowerWorth;
                        converted = true;
                    }
                }

                // If no conversion was possible, break the loop
                if (!converted) break;
            }

            if (totalWorth > 0) {
                // Update the current stack with the remaining items
                int remainingCount = totalWorth / currentWorth;
                stack.setCount(remainingCount);
            } else {
                stack.setCount(0);
            }

            Jonky.LOGGER.info("Converted stack of model data {} to lower denominations with {} remaining.", modelDataValue, totalWorth);
        } else {
            // Normal behavior: Convert to higher denominations
            if (modelDataValue >= 8) {
                Jonky.LOGGER.warn("Cannot convert stack with model data {}", modelDataValue);
                return; // No higher denomination available
            }

            int nextModelDataValue = modelDataValue + 1;
            int nextWorth = worthMap.get(nextModelDataValue);

            int itemCount = stack.getCount();
            int totalWorth = currentWorth * itemCount;

            int maxNewItems = totalWorth / nextWorth; // Number of items for the higher denomination
            int remainingWorth = totalWorth % nextWorth;

            if (maxNewItems > 0) {
                // Add the new stack with higher denomination
                ItemStack newStack = new ItemStack(stack.getItem(), maxNewItems);
                CustomModelDataComponent newCustomModelData = new CustomModelDataComponent(nextModelDataValue);
                newStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, newCustomModelData);
                inventory.insertStack(newStack);
            }

            if (remainingWorth > 0) {
                // Update the current stack with the remaining items
                int remainingCount = remainingWorth / currentWorth;
                stack.setCount(remainingCount);
            } else {
                stack.setCount(0);
            }

            Jonky.LOGGER.info("Converted stack {} to {} worth {} with {} remaining.", modelDataValue, nextModelDataValue, nextWorth, remainingWorth);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        FoodComponent foodComponent = itemStack.get(DataComponentTypes.FOOD);
        if (foodComponent != null) {
            if (user.canConsume(foodComponent.canAlwaysEat())) {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            } else {
                return TypedActionResult.fail(itemStack);
            }
        } else {
            count++;
            if(count >= 1){
                count = 0;
                user.getItemCooldownManager().set(itemStack.getItem(), 10);
                convertBanknote(itemStack, user.getInventory(), user.isSneaking());
            }

            return TypedActionResult.pass(user.getStackInHand(hand));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (stack.contains(DataComponentTypes.CUSTOM_MODEL_DATA)) {
            CustomModelDataComponent customModelData = stack.get(DataComponentTypes.CUSTOM_MODEL_DATA);
            String key;
            Formatting format;
            int customModelDataValue = Objects.requireNonNull(customModelData).value();

            key = switch (customModelDataValue) {
                case 2 -> {
                    format = Formatting.GRAY;
                    yield "5";
                }
                case 3 -> {
                    format = Formatting.RED;
                    yield "10";
                }
                case 4 -> {
                    format = Formatting.BLUE;
                    yield "20";
                }
                case 5 -> {
                    format = Formatting.GOLD;
                    yield "50";
                }
                case 6 -> {
                    format = Formatting.GREEN;
                    yield "100";
                }
                case 7 -> {
                    format = Formatting.YELLOW;
                    yield "200";
                }
                case 8 -> {
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
}
