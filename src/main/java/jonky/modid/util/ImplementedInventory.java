package jonky.modid.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * A simple {@code Container} implementation with only default methods + an item list getter.
 *
 * @author Juuz
 */
public interface ImplementedInventory extends Container {

    /**
     * Retrieves the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    NonNullList<ItemStack> getItems();

    /**
     * Creates a container from the item list.
     */
    static ImplementedInventory of(NonNullList<ItemStack> items) {
        return () -> items;
    }

    /**
     * Creates a new container with the specified size.
     */
    static ImplementedInventory ofSize(int size) {
        return of(NonNullList.withSize(size, ItemStack.EMPTY));
    }

    /**
     * Returns the container size.
     */
    @Override
    default int getContainerSize() {
        return getItems().size();
    }

    /**
     * Checks if the container is empty.
     */
    @Override
    default boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the item in the slot.
     */
    @Override
    default ItemStack getItem(int slot) {
        return getItems().get(slot);
    }

    /**
     * Removes items from a container slot.
     */
    @Override
    default ItemStack removeItem(int slot, int count) {
        ItemStack result = ContainerHelper.removeItem(getItems(), slot, count);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    /**
     * Removes all items from a container slot.
     */
    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(getItems(), slot);
    }

    /**
     * Replaces the current stack in a container slot with the provided stack.
     */
    @Override
    default void setItem(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > stack.getMaxStackSize()) {
            stack.setCount(stack.getMaxStackSize());
        }
        setChanged();
    }

    /**
     * Clears the container.
     */
    @Override
    default void clearContent() {
        getItems().clear();
    }

    /**
     * Marks the state as dirty.
     */
    @Override
    default void setChanged() {
        // Override if you want behavior.
    }

    /**
     * @return true if the player can use the container, false otherwise.
     */
    @Override
    default boolean stillValid(Player player) {
        return true;
    }
}