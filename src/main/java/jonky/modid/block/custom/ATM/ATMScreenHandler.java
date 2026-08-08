package jonky.modid.block.custom.ATM;

import jonky.modid.component.ModComponents;
import jonky.modid.item.ModItems;
import jonky.modid.screen.ModScreens;
import jonky.modid.util.BanknoteUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ATMScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    final Slot inputSlot;
    final Slot outputSlot;
    private int selectedRecipe;
    private final ContainerData propertyDelegate;

    public ATMScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(9), new SimpleContainerData(1));
    }

    public ATMScreenHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(ModScreens.ATM_SCREEN_HANDLER, syncId);
        checkContainerSize(inventory, 9);
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);

        this.propertyDelegate = propertyDelegate;
        this.addDataSlots(propertyDelegate);

        int m;
        int l;

        this.inputSlot = this.addSlot(new Slot(inventory, 1, 20, 33) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == ModItems.BANKNOTE;
            }

            @Override
            public void setByPlayer(ItemStack stack) {
                storeBanknote(stack);
            }
        });
        this.outputSlot = this.addSlot(new Slot(inventory, 2, 143, 33) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                deductStoredJonkyFromItem(stack);
                stack.onCraftedBy(player, stack.getCount());
                super.onTake(player, stack);

                refillOutputSlot();
            }
        });

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

    private static void offerOrDropStack(Player player, ItemStack stack) {
        boolean dropped = player.isRemoved() && player.getRemovalReason() != Entity.RemovalReason.CHANGED_DIMENSION;
        if (player instanceof ServerPlayer serverPlayer) {
            dropped = dropped || serverPlayer.hasDisconnected();
        }
        if (!dropped && player instanceof ServerPlayer) {
            player.getInventory().add(stack);
        } else {
            player.drop(stack, false);
        }
    }

    @Override
    public void removed(Player player) {
        if (player instanceof ServerPlayer) {
            outputSlot.set(ItemStack.EMPTY);
            ItemStack itemStack = this.getCarried();
            if (!itemStack.isEmpty()) {
                offerOrDropStack(player, itemStack);
                this.setCarried(ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    private void storeBanknote(ItemStack stack) {
        int storedJonky = propertyDelegate.get(0);
        Integer banknoteValue = stack.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
        if (banknoteValue == null) return;
        storedJonky += (banknoteValue * stack.getCount());
        setStoredJonky(storedJonky);
    }

    private void setStoredJonky(int value) {
        propertyDelegate.set(0, value);
    }

    public int getStoredJonky() {
        return propertyDelegate.get(0);
    }

    private void refillOutputSlot() {
        if (selectedRecipe > 0) {
            int storedJonky = getStoredJonky();
            int refillAmount = Math.min(storedJonky / selectedRecipe, 64);
            if (refillAmount > 0) {
                outputSlot.set(
                        BanknoteUtils.createBanknoteStack(selectedRecipe, refillAmount)
                );
            } else {
                outputSlot.set(ItemStack.EMPTY);
            }
        }
    }

    private void deductStoredJonkyFromItem(ItemStack stack) {
        Integer noteValue = stack.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
        if (noteValue != null) {
            int total = noteValue * stack.getCount();
            setStoredJonky(getStoredJonky() - total);
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        ItemStack selectedBanknote = BanknoteUtils.ATMItemList.get(id);
        Integer selectedBanknoteValue = selectedBanknote.get(ModComponents.BANKNOTE_VALUE_COMPONENT);
        if (selectedBanknoteValue == null) return false;

        int storedJonky = getStoredJonky();

        int banknoteAmount = Math.min(storedJonky / selectedBanknoteValue, 64);

        if (banknoteAmount > 0) {
            selectedRecipe = selectedBanknoteValue;
            this.outputSlot.set(BanknoteUtils.createBanknoteStack(selectedBanknoteValue, banknoteAmount));
            return true;
        }

        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack movedStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack original = slot.getItem();
            movedStack = original.copy();

            if (slot == this.outputSlot) {
                deductStoredJonkyFromItem(original);
                if (!this.moveItemStackTo(original, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.setChanged();
                refillOutputSlot();
                return movedStack;
            }

            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(original, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(original, 0, this.inventory.getContainerSize(), false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (original.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return movedStack;
    }
}