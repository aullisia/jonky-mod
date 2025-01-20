package jonky.modid.item.custom;

import jonky.modid.Jonky;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;

public class BanknoteItem extends Item {
    public BanknoteItem(Settings settings) {
        super(settings);
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
