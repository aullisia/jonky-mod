package jonky.modid.block.custom.ATM;

import jonky.modid.block.ModBlockEntities;
import jonky.modid.util.ImplementedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class ATMBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    private int containedJonky = 0;

    public int getContainedJonky() {
        return containedJonky;
    }

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return containedJonky;
        }

        @Override
        public void set(int index, int value) {
            containedJonky = value;
        }

        //this is supposed to return the amount of integers you have in your delegate, in our example only one
        @Override
        public int getCount() {
            return 1;
        }
    };

    public ATMBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ATM_BLOCK_ENTITY, pos, state);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ATMScreenHandler(containerId, playerInventory, this, containerData);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        containedJonky = input.getIntOr("contained_jonky", 0);
        ContainerHelper.loadAllItems(input, this.getItems());
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        output.putInt("contained_jonky", containedJonky);
        ContainerHelper.saveAllItems(output, this.getItems());
    }
}