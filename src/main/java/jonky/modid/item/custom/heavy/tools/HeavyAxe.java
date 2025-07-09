package jonky.modid.item.custom.heavy.tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public class HeavyAxe extends HeavyTool {
    public HeavyAxe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(settings.axe(material, attackDamage, attackSpeed));
    }

    public static List<BlockPos> getBlocksToBeDestroyed(BlockPos initialBlockPos, ServerPlayerEntity player) {
        World world = player.getWorld();
        BlockState initialState = world.getBlockState(initialBlockPos);
        Block initialBlock = initialState.getBlock();

        if (!isLogBlock(initialBlock)) {
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

                        if (isLogBlock(neighborBlock) || isLeafBlock(neighborBlock)) {
                            visited.add(neighborPos);
                            toCheck.add(neighborPos);
                        }
                    }
                }
            }

        }

        return new ArrayList<>(visited);
    }

    private static boolean isLogBlock(Block block) {
        return block.getDefaultState().isIn(BlockTags.LOGS);
    }

    private static boolean isLeafBlock(Block block) {
        return block.getDefaultState().isIn(BlockTags.LEAVES);
    }
}
