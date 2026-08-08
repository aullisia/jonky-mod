package jonky.modid.item.custom.heavy.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class HeavyTool extends Item {
    public HeavyTool(Item.Properties properties) {
        super(properties);
    }

    public static List<BlockPos> getBlocksToBeDestroyed(int range, BlockPos initalBlockPos, ServerPlayer player) {
        List<BlockPos> positions = new ArrayList<>();
        return positions;
    }
}