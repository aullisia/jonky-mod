package jonky.modid.event;

import jonky.modid.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

public class ModLootTableEvents {
    public static void registerLootTableEvents() {
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            if (key.getValue().toString().equals("minecraft:chests/trial_chambers/reward_ominous_unique") && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.HEAVY_UPGRADE))
                        .conditionally(RandomChanceLootCondition.builder(0.045f))
                        .rolls(ConstantLootNumberProvider.create(1));

                builder.pool(poolBuilder);
            }
        });
    }
}
