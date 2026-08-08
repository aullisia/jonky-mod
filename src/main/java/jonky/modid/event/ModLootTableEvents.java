package jonky.modid.event;

import jonky.modid.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModLootTableEvents {
    public static void registerLootTableEvents() {
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            if (key.identifier().toString().equals("minecraft:chests/trial_chambers/reward_ominous_unique") && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(ModItems.HEAVY_UPGRADE))
                        .when(LootItemRandomChanceCondition.randomChance(0.045f))
                        .setRolls(ConstantValue.exactly(1));

                builder.withPool(poolBuilder);
            }
        });
    }
}