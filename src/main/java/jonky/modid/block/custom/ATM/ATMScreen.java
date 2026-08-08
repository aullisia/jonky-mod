package jonky.modid.block.custom.ATM;

import jonky.modid.util.BanknoteUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ATMScreen extends AbstractContainerScreen<ATMScreenHandler> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("jonky", "textures/gui/container/atm.png");

    private static final Identifier RECIPE_SELECTED_TEXTURE = Identifier.withDefaultNamespace("container/stonecutter/recipe_selected");
    private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = Identifier.withDefaultNamespace("container/stonecutter/recipe_highlighted");
    private static final Identifier RECIPE_TEXTURE = Identifier.withDefaultNamespace("container/stonecutter/recipe");

    private int selectedButtonId = -1;

    public ATMScreen(ATMScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        int i = this.leftPos;
        int j = this.topPos;

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        int l = this.leftPos + 56;
        int m = this.topPos + 14;

        this.renderRecipeBackground(graphics, mouseX, mouseY, l, m);
        this.renderRecipeIcons(graphics, l, m);

        String storedJonky = Integer.toString(this.menu.getStoredJonky());
        graphics.text(
                this.font,
                storedJonky,
                l + ((92 + 3) - (storedJonky.length() * 3)),
                m + 45,
                ARGB.color(139, 139, 139),
                false
        );
    }

    private void renderRecipeBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int startX, int startY) {
        int index = 0;
        Map<Integer, ItemStack> itemList = BanknoteUtils.ATMItemList;
        List<Integer> sortedKeys = new ArrayList<>(itemList.keySet());
        sortedKeys.sort(Integer::compare);

        for (Integer buttonId : sortedKeys) {
            int posX = startX + (index % 4) * 16;
            int posY = startY + (index / 4) * 16;

            Identifier background = RECIPE_TEXTURE;
            if (buttonId == selectedButtonId) {
                background = RECIPE_SELECTED_TEXTURE;
            } else if (mouseX >= posX && mouseY >= posY && mouseX < posX + 16 && mouseY < posY + 16) {
                background = RECIPE_HIGHLIGHTED_TEXTURE;
            }

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, posX, posY, 16, 16);

            index++;
        }
    }

    private void renderRecipeIcons(GuiGraphicsExtractor graphics, int startX, int startY) {
        int index = 0;
        Map<Integer, ItemStack> itemList = BanknoteUtils.ATMItemList;
        List<Integer> sortedKeys = new ArrayList<>(itemList.keySet());
        sortedKeys.sort(Integer::compare);

        for (Integer buttonId : sortedKeys) {
            int posX = startX + (index % 4) * 16;
            int posY = startY + (index / 4) * 16;

            ItemStack itemStack = itemList.get(buttonId);
            graphics.item(itemStack, posX, posY);

            index++;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    private void sendButtonPressPacket(int id) {
        assert this.minecraft != null;
        assert this.minecraft.gameMode != null;
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int startX = this.leftPos + 56;
        int startY = this.topPos + 14;
        int index = 0;
        Map<Integer, ItemStack> itemList = BanknoteUtils.ATMItemList;
        List<Integer> sortedKeys = new ArrayList<>(itemList.keySet());
        sortedKeys.sort(Integer::compare);

        for (Integer buttonId : sortedKeys) {
            int posX = startX + (index % 4) * 16;
            int posY = startY + (index / 4) * 16;

            if (event.x() >= posX && event.x() < posX + 16 && event.y() >= posY && event.y() < posY + 16) {
                selectedButtonId = buttonId;
                Minecraft.getInstance().getSoundManager().play(
                        SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                );
                sendButtonPressPacket(buttonId);
                return true;
            }
            index++;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);

        int startX = this.leftPos + 56;
        int startY = this.topPos + 14;
        int index = 0;
        Map<Integer, ItemStack> itemList = BanknoteUtils.ATMItemList;
        List<Integer> sortedKeys = new ArrayList<>(itemList.keySet());
        sortedKeys.sort(Integer::compare);

        for (Integer buttonId : sortedKeys) {
            int posX = startX + (index % 4) * 16;
            int posY = startY + (index / 4) * 16;
            if (mouseX >= posX && mouseX < posX + 16 && mouseY >= posY && mouseY < posY + 16) {
                ItemStack stack = itemList.get(buttonId);
                graphics.setTooltipForNextFrame(this.font, stack, mouseX, mouseY);
                break;
            }
            index++;
        }
    }
}