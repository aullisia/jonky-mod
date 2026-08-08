package jonky.modid.item.custom.heavy.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class HeavyAxe extends HeavyTool {
    public HeavyAxe(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(properties.axe(material, attackDamage, attackSpeed));
    }

    public static List<BlockPos> getBlocksToBeDestroyed(BlockPos initialBlockPos, ServerPlayer player) {
        Level world = player.level();
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

                        BlockPos neighborPos = currentPos.offset(dx, dy, dz);
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
        return block.defaultBlockState().is(BlockTags.LOGS);
    }

    private static boolean isLeafBlock(Block block) {
        return block.defaultBlockState().is(BlockTags.LEAVES);
    }
}