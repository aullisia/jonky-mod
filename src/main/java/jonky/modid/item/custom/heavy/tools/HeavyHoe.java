package jonky.modid.item.custom.heavy.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public class HeavyHoe extends HeavyTool {
    public HeavyHoe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(settings.hoe(material, attackDamage, attackSpeed));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            BlockHitResult hitResult = (BlockHitResult) user.raycast(5.0D, 0.0F, false);

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos centerPos = hitResult.getBlockPos();

                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        BlockPos pos = centerPos.add(dx, 0, dz);
                        BlockState state = world.getBlockState(pos);
                        BlockState above = world.getBlockState(pos.up());

                        // Only till dirt/grass blocks if air is above
                        if (above.isAir() && (state.isOf(net.minecraft.block.Blocks.DIRT) || state.isOf(net.minecraft.block.Blocks.GRASS_BLOCK))) {
                            world.setBlockState(pos, net.minecraft.block.Blocks.FARMLAND.getDefaultState());

                            // Damage the hoe
                            stack.damage(1, user);
                        }
                    }
                }

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    public static List<BlockPos> getBlocksToBeDestroyed(BlockPos initialBlockPos, ServerPlayerEntity player) {
        World world = player.getWorld();
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

                        BlockPos neighborPos = currentPos.add(dx, dy, dz);
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
        return block.getDefaultState().isIn(BlockTags.LEAVES);
    }
}
