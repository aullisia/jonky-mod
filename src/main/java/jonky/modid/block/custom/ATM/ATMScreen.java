package jonky.modid.block.custom.ATM;

import com.mojang.blaze3d.systems.RenderSystem;
import jonky.modid.Jonky;
import jonky.modid.util.BanknoteUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.ColorHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMScreen extends HandledScreen<ATMScreenHandler> {
    // A path to the gui texture. In this example we use the texture from the dispenser

    //private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/dispenser.png");
    private static final Identifier TEXTURE = Identifier.of("jonky", "textures/gui/container/atm.png");
    // For versions before 1.21:
    // private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");

    private static final Identifier RECIPE_SELECTED_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe_selected");
    private static final Identifier RECIPE_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe_highlighted");
    private static final Identifier RECIPE_TEXTURE = Identifier.ofVanilla("container/stonecutter/recipe");

    ATMScreenHandler  screenHandler;

    private int selectedButtonId = -1; // -1 means none selected.

    public ATMScreen(ATMScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        screenHandler = (ATMScreenHandler) handler;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, x, y, 0.0F, 0.0F, backgroundWidth, backgroundHeight, 256, 256);
        // context.drawTexture(RenderLayer::getGuiTextured, this.texture, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256); // ForgingScreen.java

//        this.addDrawableChild(ButtonWidget.builder(Text.literal("Btn"), buttonWidget -> {
//
//        }).dimensions(this.width / 2 - 100, 196, 98, 20).build());

        // Buttons for banknotes
        int l = this.x + 56;
        int m = this.y + 14;

        renderRecipeBackground(context, mouseX, mouseY, l, m);
        renderRecipeIcons(context, l, m);

        // Render amount of Jonky in the machine
        String storedJonky = Integer.toString(screenHandler.getStoredJonky());
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(textRenderer, storedJonky, l + ((92 + 3) - (storedJonky.length() * 3)), m + 45, ColorHelper.getArgb(139,139,139), false);

//        ItemStack stack = BanknoteUtils.createBanknoteStack(20, 1);
//        context.drawItem(stack, l, m);

        // context.drawItem(slotDisplay.getFirst(contextParameterMap), k, m); // StonecutterScreen.java

        //this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).dimensions(this.width / 2 - 100, 196, 98, 20).build());
    }

    private void renderRecipeBackground(DrawContext context, int mouseX, int mouseY, int startX, int startY) {
        int index = 0;
        // You might want to iterate in order (e.g., sorted by key) if order matters:
        Map<Integer, ItemStack> itemList = BanknoteUtils.ATMItemList;
        List<Integer> sortedKeys = new ArrayList<>(itemList.keySet());
        sortedKeys.sort(Integer::compare);

        for (Integer buttonId : sortedKeys) {
            // Calculate grid position (here using 4 icons per row)
            int posX = startX + (index % 4) * 16;
            int posY = startY + (index / 4) * 16; // Change 16 to 18 if you want a taller background like stonecutter.

            // Choose which background texture to use
            Identifier background = RECIPE_TEXTURE;
            if (buttonId == selectedButtonId) {
                background = RECIPE_SELECTED_TEXTURE;
            } else if (mouseX >= posX && mouseY >= posY && mouseX < posX + 16 && mouseY < posY + 16) {
                background = RECIPE_HIGHLIGHTED_TEXTURE;
            }

            // Draw the background.
            // (Note: In StonecutterScreen the backgrounds are drawn with a slight vertical offset.
            // Adjust the y coordinate or height as needed.)
            context.drawGuiTexture(RenderLayer::getGuiTextured, background, posX, posY, 16, 16);

            index++;
        }
    }

    private void renderRecipeIcons (DrawContext context, int startX, int startY) {
        int index = 0;
        Map<Integer, ItemStack> itemList = BanknoteUtils.ATMItemList;
        List<Integer> sortedKeys = new ArrayList<>(itemList.keySet());
        sortedKeys.sort(Integer::compare);

        for (Integer buttonId : sortedKeys) {
            // Calculate grid position
            int posX = startX + (index % 4) * 16;
            int posY = startY + (index / 4) * 16;

            // Draw the item icon (the item from your hashmap)
            ItemStack itemStack = itemList.get(buttonId);
            context.drawItem(itemStack, posX, posY);

            index++;
        }
//        for (Map.Entry<Integer, ItemStack> entry : itemList.entrySet()) {
//            Integer buttonId = entry.getKey();
//            ItemStack itemStack = entry.getValue();
//            context.drawItem(itemStack, x, y);
//            x += 16;
//            if(this.x + 52 + 16 * 4 <= x) {
//                y += 16;
//                x = this.x + 52;
//            }
//        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    private void sendButtonPressPacket(int id) {
        assert this.client != null;
        assert this.client.interactionManager != null;
        this.client.interactionManager.clickButton(this.handler.syncId, id);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = this.x + 56;
        int startY = this.y + 14;
        int index = 0;
        Map<Integer, ItemStack> itemList = BanknoteUtils.ATMItemList;
        List<Integer> sortedKeys = new ArrayList<>(itemList.keySet());
        sortedKeys.sort(Integer::compare);

        for (Integer buttonId : sortedKeys) {
            int posX = startX + (index % 4) * 16;
            int posY = startY + (index / 4) * 16;

            if (mouseX >= posX && mouseX < posX + 16 && mouseY >= posY && mouseY < posY + 16) {
                // Set the clicked button as selected, play sound, and do any other action
                selectedButtonId = buttonId;
                MinecraftClient.getInstance().getSoundManager().play(
                        PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                );
//                Jonky.LOGGER.warn("Item button " + buttonId + " clicked!");
                sendButtonPressPacket(buttonId);
                return true;
            }
            index++;
        }
        return super.mouseClicked(mouseX, mouseY, button);
//        if (isMouseOverItem(mouseX, mouseY)) {
//            // Play click sound
//            MinecraftClient.getInstance().getSoundManager().play(
//                    PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F)
//            );
//            // Handle the click action here (e.g., send packet to server)
//            Jonky.LOGGER.warn("Item button clicked!");
//            return true;
//        }
//        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int mouseX, int mouseY) {
        super.drawMouseoverTooltip(context, mouseX, mouseY);

        int startX = this.x + 56;
        int startY = this.y + 14;
        int index = 0;
        Map<Integer, ItemStack> itemList = BanknoteUtils.ATMItemList;
        List<Integer> sortedKeys = new ArrayList<>(itemList.keySet());
        sortedKeys.sort(Integer::compare);

        // Iterate over the buttons and check if the mouse is over one.
        for (Integer buttonId : sortedKeys) {
            int posX = startX + (index % 4) * 16;
            int posY = startY + (index / 4) * 16;
            if (mouseX >= posX && mouseX < posX + 16 && mouseY >= posY && mouseY < posY + 16) {
                // Get the corresponding ItemStack and draw its tooltip.
                ItemStack stack = itemList.get(buttonId);
                context.drawItemTooltip(this.textRenderer, stack, mouseX, mouseY);
                // Since icons do not overlap, we can exit after drawing one tooltip.
                break;
            }
            index++;
        }
    }
}
