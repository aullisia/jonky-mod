package jonky.modid.event.heavycore;

import jonky.modid.component.ModComponents;
import jonky.modid.item.custom.heavy.tools.HeavyAxe;
import jonky.modid.item.custom.heavy.tools.HeavyHoe;
import jonky.modid.item.custom.heavy.tools.HeavyPickaxe;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class HoeUsageEvent implements PlayerBlockBreakEvents.Before {
    // Done with the help of https://github.com/CoFH/CoFHCore/blob/c23d117dcd3b3b3408a138716b15507f709494cd/src/main/java/cofh/core/event/AreaEffectEvents.java
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (world.isClient) return true;

        ItemStack mainHandItem = player.getMainHandStack();

        if (mainHandItem.getItem() instanceof HeavyHoe heavyHoe
                && Boolean.TRUE.equals(mainHandItem.get(ModComponents.TOOL_ABILITY_TOGGLE_COMPONENT))
                && player instanceof ServerPlayerEntity serverPlayer) {

            if (HARVESTED_BLOCKS.contains(pos)) {
                return true;
            }

            Set<BlockPos> blocksToBreak = new HashSet<>(HeavyHoe.getBlocksToBeDestroyed(pos, serverPlayer));

            for (BlockPos targetPos : blocksToBreak) {
                if (pos.equals(targetPos)) continue;

                BlockState targetState = world.getBlockState(targetPos);

                if (!heavyHoe.isCorrectForDrops(mainHandItem, targetState)) continue;

                HARVESTED_BLOCKS.add(targetPos);
                int currentDamage = mainHandItem.getDamage();
                int maxDamage = mainHandItem.getMaxDamage();

                if (currentDamage >= maxDamage - 10) {
                    continue;
                }

                serverPlayer.interactionManager.tryBreakBlock(targetPos);
                HARVESTED_BLOCKS.remove(targetPos);
            }
        }

        return true;
    }
}
