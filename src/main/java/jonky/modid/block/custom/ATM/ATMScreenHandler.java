package jonky.modid.block.custom.ATM;

import jonky.modid.Jonky;
import jonky.modid.component.ModComponents;
import jonky.modid.item.ModItems;
import jonky.modid.screen.ModScreens;
import jonky.modid.util.BanknoteUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;

public class ATMScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    final Slot inputSlot;
    final Slot outputSlot;
    private int selectedRecipe;
    PropertyDelegate propertyDelegate;

    // This constructor gets called on the client when the server wants it to open the screenHandler,
    // The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    // sync this empty inventory with the inventory on the server.
    public ATMScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(9), new ArrayPropertyDelegate(1));
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    // and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public ATMScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreens.ATM_SCREEN_HANDLER, syncId);
        checkSize(inventory, 9);
        this.inventory = inventory;
        // some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);

        int m;
        int l;
        // Our inventory
        this.inputSlot = this.addSlot(new Slot(inventory, 1, 20, 33){
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() == ModItems.BANKNOTE;
            }

            @Override
            public void setStack(ItemStack stack) {
//                this.setStack(stack, this.getStack());
                storeBanknote(stack);
            }
        });
        this.outputSlot = this.addSlot(new Slot(inventory, 2, 143, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                int storedJonky = getStoredJonky();
                int newStoredJonky = storedJonky - (selectedRecipe * stack.getCount());
                if (newStoredJonky < 0) return;
                setStoredJonky(newStoredJonky);

                stack.onCraftByPlayer(player.getWorld(), player, stack.getCount());
                super.onTakeItem(player, stack);
            }
        });
//        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33) {
//        for (m = 0; m < 3; ++m) {
//            for (l = 0; l < 3; ++l) {
//                this.addSlot(new Slot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18));
//            }



        // The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void storeBanknote(ItemStack stack) {
        int storedJonky = propertyDelegate.get(0);
        Integer banknoteValue = stack.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
        if(banknoteValue == null) return;
        storedJonky += (banknoteValue * stack.getCount());
        setStoredJonky(storedJonky);
    }

    private void setStoredJonky(int value){
        propertyDelegate.set(0, value);
    }


    public int getStoredJonky(){
        return propertyDelegate.get(0);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        ItemStack selectedBanknote = BanknoteUtils.ATMItemList.get(id);
        Integer selectedBanknoteValue = selectedBanknote.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
        if(selectedBanknoteValue == null) return false;
        Jonky.LOGGER.warn("Selected Banknote value: " + selectedBanknoteValue);
        // Conversion logic

        int storedJonky = getStoredJonky();

        int banknoteAmount = Math.min(storedJonky / selectedBanknoteValue, 64);

        if (banknoteAmount > 0) {
            selectedRecipe = selectedBanknoteValue;
            this.outputSlot.setStackNoCallbacks(BanknoteUtils.createBanknoteStack(selectedBanknoteValue, banknoteAmount));
            return true;
        }

        return false;
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }
}
