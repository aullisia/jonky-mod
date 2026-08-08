package jonky.modid.block.custom.CopperRail;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

public class CopperRailBlock extends PoweredRailBlock {
    public static final MapCodec<CopperRailBlock> CODEC = simpleCodec(CopperRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public CopperRailBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(POWERED, false)
                .setValue(WATERLOGGED, false));
    }

    protected boolean findPoweredRailSignal(Level level, BlockPos pos, BlockState state, boolean forward, int searchDepth) {
        if (searchDepth >= 8) {
            return false;
        } else {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            boolean checkBelow = true;
            RailShape railShape = state.getValue(SHAPE);

            switch (railShape) {
                case NORTH_SOUTH:
                    if (forward) {
                        k++;
                    } else {
                        k--;
                    }
                    break;
                case EAST_WEST:
                    if (forward) {
                        i--;
                    } else {
                        i++;
                    }
                    break;
                case ASCENDING_EAST:
                    if (forward) {
                        i--;
                    } else {
                        i++;
                        j++;
                        checkBelow = false;
                    }

                    railShape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_WEST:
                    if (forward) {
                        i--;
                        j++;
                        checkBelow = false;
                    } else {
                        i++;
                    }

                    railShape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_NORTH:
                    if (forward) {
                        k++;
                    } else {
                        k--;
                        j++;
                        checkBelow = false;
                    }

                    railShape = RailShape.NORTH_SOUTH;
                    break;
                case ASCENDING_SOUTH:
                    if (forward) {
                        k++;
                        j++;
                        checkBelow = false;
                    } else {
                        k--;
                    }

                    railShape = RailShape.NORTH_SOUTH;
            }

            return this.isSameRailWithPower(level, new BlockPos(i, j, k), forward, searchDepth, railShape)
                    ? true
                    : checkBelow && this.isSameRailWithPower(level, new BlockPos(i, j - 1, k), forward, searchDepth, railShape);
        }
    }

    protected boolean isSameRailWithPower(Level level, BlockPos pos, boolean forward, int searchDepth, RailShape shape) {
        BlockState blockState = level.getBlockState(pos);
        if (!blockState.is(this)) {
            return false;
        } else {
            RailShape railShape = blockState.getValue(SHAPE);
            if (shape != RailShape.EAST_WEST
                    || railShape != RailShape.NORTH_SOUTH && railShape != RailShape.ASCENDING_NORTH && railShape != RailShape.ASCENDING_SOUTH) {
                if (shape != RailShape.NORTH_SOUTH
                        || railShape != RailShape.EAST_WEST && railShape != RailShape.ASCENDING_EAST && railShape != RailShape.ASCENDING_WEST) {
                    if (!blockState.getValue(POWERED)) {
                        return false;
                    } else {
                        return level.hasNeighborSignal(pos)
                                ? true
                                : this.findPoweredRailSignal(level, pos, blockState, forward, searchDepth + 1);
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    protected void updateState(BlockState state, Level level, BlockPos pos, Block neighbor) {
        RailShape shape = state.getValue(SHAPE);
        if (shape.isSlope()) {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            RailShape flatShape = (facing == Direction.EAST || facing == Direction.WEST)
                    ? RailShape.EAST_WEST
                    : RailShape.NORTH_SOUTH;
            state = state.setValue(SHAPE, flatShape);
            level.setBlock(pos, state, 3);
        }

        boolean currentlyPowered = state.getValue(POWERED);
        boolean shouldPower = level.hasNeighborSignal(pos)
                || this.findPoweredRailSignal(level, pos, state, true, 0)
                || this.findPoweredRailSignal(level, pos, state, false, 0);

        if (shouldPower != currentlyPowered) {
            level.setBlock(pos, state.setValue(POWERED, shouldPower), Block.UPDATE_ALL);
            level.updateNeighborsAt(pos.below(), this);
            if (state.getValue(SHAPE).isSlope()) {
                level.updateNeighborsAt(pos.above(), this);
            }
        }
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        RailShape railShape = state.getValue(SHAPE);
        RailShape railShape2 = this.rotate(railShape, rotation);
        return state.setValue(SHAPE, railShape2);
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        RailShape railShape = state.getValue(SHAPE);
        RailShape railShape2 = this.mirror(railShape, mirror);
        return state.setValue(SHAPE, railShape2);
    }

    private static boolean shouldBeRemoved(BlockPos pos, Level level, RailShape shape) {
        if (!Block.canSupportRigidBlock(level, pos.below())) {
            return true;
        } else {
            switch (shape) {
                case ASCENDING_EAST:
                    return !Block.canSupportRigidBlock(level, pos.east());
                case ASCENDING_WEST:
                    return !Block.canSupportRigidBlock(level, pos.west());
                case ASCENDING_NORTH:
                    return !Block.canSupportRigidBlock(level, pos.north());
                case ASCENDING_SOUTH:
                    return !Block.canSupportRigidBlock(level, pos.south());
                default:
                    return false;
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide() && state.is(this)) {
            Direction newFacing = null;
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos neighborPos = pos.relative(dir);
                BlockState neighborState = level.getBlockState(neighborPos);
                if (neighborState.is(this)) {
                    newFacing = dir;
                    break;
                }
            }

            if (newFacing != null) {
                Direction currentFacing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                if (currentFacing != newFacing) {
                    BlockState newState = state.setValue(BlockStateProperties.HORIZONTAL_FACING, newFacing.getOpposite());
                    level.setBlock(pos, newState, Block.UPDATE_ALL);
                    state = newState;
                }
            }

            RailShape shape = state.getValue(SHAPE);
            if (shouldBeRemoved(pos, level, shape)) {
                Block.dropResources(state, level, pos);
                level.removeBlock(pos, movedByPiston);
            } else {
                this.updateState(state, level, pos, sourceBlock);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection());
    }
}