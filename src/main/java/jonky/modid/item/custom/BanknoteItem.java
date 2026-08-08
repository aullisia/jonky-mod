package jonky.modid.item.custom;

import jonky.modid.component.ModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class BanknoteItem extends Item {

    public BanknoteItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> textConsumer, TooltipFlag flag
    ) {
        Integer value = stack.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
        if (value == null) return;

        String key;
        ChatFormatting format;

        key = switch (value) {
            case 5 -> {
                format = ChatFormatting.YELLOW;
                yield "5";
            }
            case 10 -> {
                format = ChatFormatting.GRAY;
                yield "10";
            }
            case 20 -> {
                format = ChatFormatting.RED;
                yield "20";
            }
            case 50 -> {
                format = ChatFormatting.GOLD;
                yield "50";
            }
            case 100 -> {
                format = ChatFormatting.GREEN;
                yield "100";
            }
            case 200 -> {
                format = ChatFormatting.BLUE;
                yield "200";
            }
            case 500 -> {
                format = ChatFormatting.DARK_GRAY;
                yield "500";
            }
            default -> {
                format = ChatFormatting.DARK_RED;
                yield "";
            }
        };

        if (!key.isEmpty()) {
            textConsumer.accept(Component.translatable("item.banknote.amount.info." + key).withStyle(format));
        } else {
            textConsumer.accept(Component.translatable("item.banknote.amount.info").withStyle(format));
        }
    }
}