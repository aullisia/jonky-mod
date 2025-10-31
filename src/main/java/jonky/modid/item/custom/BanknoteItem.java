package jonky.modid.item.custom;

import jonky.modid.component.ModComponents;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.function.Consumer;

public class BanknoteItem extends Item {

    public BanknoteItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(
            ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type
    ){
    Integer value = stack.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
        if (value == null) return;

        String key;
        Formatting format;

        key = switch (value) {
            case 5 -> {
                format = Formatting.YELLOW;
                yield "5";
            }
            case 10 -> {
                format = Formatting.GRAY;
                yield "10";
            }
            case 20 -> {
                format = Formatting.RED;
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
                format = Formatting.BLUE;
                yield "200";
            }
            case 500 -> {
                format = Formatting.DARK_GRAY;
                yield "500";
            }
            default -> {
                format = Formatting.DARK_RED;
                yield "";
            }
        };

        if (!key.isEmpty()) {
            textConsumer.accept(Text.translatable("item.banknote.amount.info." + key).formatted(format));
        } else {
            textConsumer.accept(Text.translatable("item.banknote.amount.info").formatted(format));
        }
    }
}
