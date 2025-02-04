package jonky.modid.block.custom.ATM;

import com.mojang.blaze3d.systems.RenderSystem;
import jonky.modid.Jonky;
import jonky.modid.util.BanknoteUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

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

    private static final Map<Integer, ItemStack> itemList = new HashMap<>();
    static {
        itemList.put(1, BanknoteUtils.createBanknoteStack(5, 1));
        itemList.put(2, BanknoteUtils.createBanknoteStack(10, 1));
        itemList.put(3, BanknoteUtils.createBanknoteStack(20, 1));
        itemList.put(4, BanknoteUtils.createBanknoteStack(50, 1));
        itemList.put(5, BanknoteUtils.createBanknoteStack(100, 1));
        itemList.put(6, BanknoteUtils.createBanknoteStack(200, 1));
        itemList.put(7, BanknoteUtils.createBanknoteStack(500, 1));
    }

    public ATMScreen(ATMScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
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

        // Buttons
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Btn"), buttonWidget -> {

        }).dimensions(this.width / 2 - 100, 196, 98, 20).build());

        int l = this.x + 52;
        int m = this.y + 14;

        ItemStack stack = BanknoteUtils.createBanknoteStack(20, 1);
        context.drawItem(stack, l, m);

        // context.drawItem(slotDisplay.getFirst(contextParameterMap), k, m); // StonecutterScreen.java

        //this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).dimensions(this.width / 2 - 100, 196, 98, 20).build());
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverItem(mouseX, mouseY)) {
            // Play click sound
            MinecraftClient.getInstance().getSoundManager().play(
                    PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F)
            );
            // Handle the click action here (e.g., send packet to server)
            Jonky.LOGGER.warn("Item button clicked!");
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int mouseX, int mouseY) {
        super.drawMouseoverTooltip(context, mouseX, mouseY);
        if (isMouseOverItem(mouseX, mouseY)) {
            ItemStack stack = BanknoteUtils.createBanknoteStack(20, 1);
            context.drawItemTooltip(this.textRenderer, stack, mouseX, mouseY);
        }
    }

    private boolean isMouseOverItem(double mouseX, double mouseY) {
        int l = this.x + 52;
        int m = this.y + 14;

        // Calculate item bounds relative to the GUI
        int itemX = l;
        int itemY = m;
        int itemWidth = 16; // Default item rendering width
        int itemHeight = 16; // Default item rendering height
        return mouseX >= itemX && mouseX < itemX + itemWidth
                && mouseY >= itemY && mouseY < itemY + itemHeight;
    }
}
