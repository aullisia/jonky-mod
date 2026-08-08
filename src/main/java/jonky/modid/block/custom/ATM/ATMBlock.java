package jonky.modid.block.custom.ATM;

import com.mojang.serialization.MapCodec;
import jonky.modid.block.ModBlocks;
import jonky.modid.util.BanknoteUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ATMBlock extends Block implements EntityBlock {

    public static final MapCodec<ATMBlock> CODEC = simpleCodec(ATMBlock::new);

    public ATMBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public MapCodec<? extends ATMBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ATMBlockEntity(pos, state);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            // This will call the getMenuProvider method from Block, which will return our blockEntity casted to a MenuProvider.
            MenuProvider menuProvider = state.getMenuProvider(level, pos);

            if (menuProvider != null) {
                // With this call the server will request the client to open the appropriate ScreenHandler.
                player.openMenu(menuProvider);
            }
        }
        return InteractionResult.SUCCESS;
    }

    // Drops
    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (blockEntity instanceof ATMBlockEntity atmBlockEntity) {
            int jonky = atmBlockEntity.getContainedJonky();

            List<ItemStack> jonkyStackList = new ArrayList<>();
            int[] values = {500, 200, 100, 50, 20, 10, 5};

            for (int value : values) {
                int count = jonky / value;
                jonky %= value;

                while (count > 0) {
                    int stackSize = Math.min(count, 64);
                    jonkyStackList.add(BanknoteUtils.createBanknoteStack(value, stackSize));
                    count -= stackSize;
                }
            }

            for (ItemStack stack : jonkyStackList) {
                Block.popResource(level, pos, stack);
            }

            Block.popResource(level, pos, new ItemStack(ModBlocks.ATM_BLOCK));
        }

        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof ATMBlockEntity atmBlockEntity
                ? atmBlockEntity.getContainedJonky()
                : 0;
    }

    // Rotation placement
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }
}