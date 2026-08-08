package jonky.modid.item.custom.heavy.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import jonky.modid.component.ModComponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class HeavyHoe extends HoeItem {
    public HeavyHoe(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(material, attackDamage, attackSpeed, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (Boolean.TRUE.equals(context.getItemInHand().get(ModComponents.TOOL_ABILITY_TOGGLE_COMPONENT))) {
            return InteractionResult.PASS;
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (!world.isClientSide() && Boolean.TRUE.equals(stack.get(ModComponents.TOOL_ABILITY_TOGGLE_COMPONENT))) {
            BlockHitResult hitResult = (BlockHitResult) user.pick(5.0D, 0.0F, false);

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos centerPos = hitResult.getBlockPos();

                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        BlockPos pos = centerPos.offset(dx, 0, dz);
                        BlockState state = world.getBlockState(pos);
                        BlockState above = world.getBlockState(pos.above());

                        // Only till dirt/grass blocks if air is above
                        if (above.isAir() && (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK))) {
                            world.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), 3);

                            // Damage the hoe
                            stack.hurtAndBreak(1, user, hand);
                        }
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public static List<BlockPos> getBlocksToBeDestroyed(BlockPos initialBlockPos, ServerPlayer player) {
        Level world = player.level();
        BlockState initialState = world.getBlockState(initialBlockPos);
        Block initialBlock = initialState.getBlock();

        if (!isLeafBlock(initialBlock)) {
            return Collections.emptyList();
        }

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toCheck = new LinkedList<>();

        toCheck.add(initialBlockPos);
        visited.add(initialBlockPos);

        while (!toCheck.isEmpty()) {
            BlockPos currentPos = toCheck.poll();

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;

                        BlockPos neighborPos = currentPos.offset(dx, dy, dz);
                        if (visited.contains(neighborPos)) continue;

                        BlockState neighborState = world.getBlockState(neighborPos);
                        Block neighborBlock = neighborState.getBlock();

                        if (isLeafBlock(neighborBlock)) {
                            visited.add(neighborPos);
                            toCheck.add(neighborPos);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(visited);
    }

    private static boolean isLeafBlock(Block block) {
        return block.defaultBlockState().is(BlockTags.LEAVES);
    }
}