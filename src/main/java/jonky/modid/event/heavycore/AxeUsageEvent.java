package jonky.modid.event.heavycore;

import jonky.modid.component.ModComponents;
import jonky.modid.item.custom.heavy.tools.HeavyAxe;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class AxeUsageEvent implements PlayerBlockBreakEvents.Before {
    // Done with the help of https://github.com/CoFH/CoFHCore/blob/c23d117dcd3b3b3408a138716b15507f709494cd/src/main/java/cofh/core/event/AreaEffectEvents.java
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    @Override
    public boolean beforeBlockBreak(Level world, Player player, BlockPos pos,
                                    BlockState state, @Nullable BlockEntity blockEntity) {
        // Only act server-side
        if (world.isClientSide()) return true;

        ItemStack mainHandItem = player.getMainHandItem();

        if (mainHandItem.getItem() instanceof HeavyAxe heavyAxe
                && Boolean.TRUE.equals(mainHandItem.get(ModComponents.TOOL_ABILITY_TOGGLE_COMPONENT))
                && player instanceof ServerPlayer serverPlayer) {

            if (HARVESTED_BLOCKS.contains(pos)) {
                return true;
            }

            Set<BlockPos> blocksToBreak = new HashSet<>(HeavyAxe.getBlocksToBeDestroyed(pos, serverPlayer));

            for (BlockPos targetPos : blocksToBreak) {
                if (pos.equals(targetPos)) continue;

                BlockState targetState = world.getBlockState(targetPos);

                if (!heavyAxe.isCorrectToolForDrops(mainHandItem, targetState)) continue;

                HARVESTED_BLOCKS.add(targetPos);

                int currentDamage = mainHandItem.getDamageValue();
                int maxDamage = mainHandItem.getMaxDamage();

                if (currentDamage >= maxDamage - 10) {
                    HARVESTED_BLOCKS.remove(targetPos);
                    continue;
                }

                serverPlayer.gameMode.destroyBlock(targetPos);
                HARVESTED_BLOCKS.remove(targetPos);
            }
        }

        return true;
    }
}