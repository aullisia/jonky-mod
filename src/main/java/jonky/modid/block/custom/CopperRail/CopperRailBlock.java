package jonky.modid.block.custom.CopperRail;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class CopperRailBlock extends AbstractRailBlock {
    public static final MapCodec<PoweredRailBlock> CODEC = createCodec(PoweredRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = Properties.STRAIGHT_RAIL_SHAPE;
    public static final BooleanProperty POWERED = Properties.POWERED;

    @Override
    public MapCodec<PoweredRailBlock> getCodec() {
        return CODEC;
    }

    public CopperRailBlock(AbstractBlock.Settings settings) {
        super(true, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SHAPE, RailShape.NORTH_SOUTH).with(POWERED, false).with(WATERLOGGED, false));
    }

    protected boolean isPoweredByOtherRails(World world, BlockPos pos, BlockState state, boolean bl, int distance) {
        if (distance >= 8) {
            return false;
        } else {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            boolean bl2 = true;
            RailShape railShape = state.get(SHAPE);
            switch (railShape) {
                case NORTH_SOUTH:
                    if (bl) {
                        k++;
                    } else {
                        k--;
                    }
                    break;
                case EAST_WEST:
                    if (bl) {
                        i--;
                    } else {
                        i++;
                    }
                    break;
                case ASCENDING_EAST:
                    if (bl) {
                        i--;
                    } else {
                        i++;
                        j++;
                        bl2 = false;
                    }

                    railShape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_WEST:
                    if (bl) {
                        i--;
                        j++;
                        bl2 = false;
                    } else {
                        i++;
                    }

                    railShape = RailShape.EAST_WEST;
                    break;
                case ASCENDING_NORTH:
                    if (bl) {
                        k++;
                    } else {
                        k--;
                        j++;
                        bl2 = false;
                    }

                    railShape = RailShape.NORTH_SOUTH;
                    break;
                case ASCENDING_SOUTH:
                    if (bl) {
                        k++;
                        j++;
                        bl2 = false;
                    } else {
                        k--;
                    }

                    railShape = RailShape.NORTH_SOUTH;
            }

            return this.isPoweredByOtherRails(world, new BlockPos(i, j, k), bl, distance, railShape)
                    ? true
                    : bl2 && this.isPoweredByOtherRails(world, new BlockPos(i, j - 1, k), bl, distance, railShape);
        }
    }

    protected boolean isPoweredByOtherRails(World world, BlockPos pos, boolean bl, int distance, RailShape shape) {
        BlockState blockState = world.getBlockState(pos);
        if (!blockState.isOf(this)) {
            return false;
        } else {
            RailShape railShape = blockState.get(SHAPE);
            if (shape != RailShape.EAST_WEST || railShape != RailShape.NORTH_SOUTH && railShape != RailShape.ASCENDING_NORTH && railShape != RailShape.ASCENDING_SOUTH) {
                if (shape != RailShape.NORTH_SOUTH || railShape != RailShape.EAST_WEST && railShape != RailShape.ASCENDING_EAST && railShape != RailShape.ASCENDING_WEST) {
                    if (!(Boolean)blockState.get(POWERED)) {
                        return false;
                    } else {
                        return world.isReceivingRedstonePower(pos) ? true : this.isPoweredByOtherRails(world, pos, blockState, bl, distance + 1);
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
    protected void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        RailShape shape = state.get(SHAPE);
        if (shape.isAscending()) {
            Direction facing = state.get(Properties.HORIZONTAL_FACING);
            RailShape flatShape = (facing == Direction.EAST || facing == Direction.WEST)
                    ? RailShape.EAST_WEST
                    : RailShape.NORTH_SOUTH;
            state = state.with(SHAPE, flatShape);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }

        boolean currentlyPowered = state.get(POWERED);
        boolean shouldPower = world.isReceivingRedstonePower(pos)
                || this.isPoweredByOtherRails(world, pos, state, true, 0)
                || this.isPoweredByOtherRails(world, pos, state, false, 0);

        if (shouldPower != currentlyPowered) {
            world.setBlockState(pos, state.with(POWERED, shouldPower), Block.NOTIFY_ALL);
            world.updateNeighbors(pos.down(), this);
            if (state.get(SHAPE).isAscending()) {
                world.updateNeighbors(pos.up(), this);
            }
        }
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.rotateShape(railShape, rotation);
        return state.with(SHAPE, railShape2);
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        RailShape railShape = state.get(SHAPE);
        RailShape railShape2 = this.mirrorShape(railShape, mirror);
        return state.with(SHAPE, railShape2);
    }

    private static boolean shouldDropRail(BlockPos pos, World world, RailShape shape) {
        if (!hasTopRim(world, pos.down())) {
            return true;
        } else {
            switch (shape) {
                case ASCENDING_EAST:
                    return !hasTopRim(world, pos.east());
                case ASCENDING_WEST:
                    return !hasTopRim(world, pos.west());
                case ASCENDING_NORTH:
                    return !hasTopRim(world, pos.north());
                case ASCENDING_SOUTH:
                    return !hasTopRim(world, pos.south());
                default:
                    return false;
            }
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (!world.isClient && state.isOf(this)) {
            Direction newFacing = null;
            for (Direction dir : Direction.Type.HORIZONTAL) {
                BlockPos neighborPos = pos.offset(dir);
                BlockState neighborState = world.getBlockState(neighborPos);
                if (neighborState.isOf(this)) {
                    newFacing = dir;
                    break;
                }
            }

            if (newFacing != null) {
                Direction currentFacing = state.get(Properties.HORIZONTAL_FACING);
                if (currentFacing != newFacing) {
                    BlockState newState = state.with(Properties.HORIZONTAL_FACING, newFacing.getOpposite());
                    world.setBlockState(pos, newState, Block.NOTIFY_ALL);
                    state = newState;
                }
            }

            RailShape shape = state.get(SHAPE);
            if (shouldDropRail(pos, world, shape)) {
                dropStacks(state, world, pos);
                world.removeBlock(pos, notify);
            } else {
                this.updateBlockState(state, world, pos, sourceBlock);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED, Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
    }
}
