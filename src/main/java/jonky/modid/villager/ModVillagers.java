package jonky.modid.villager;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import jonky.modid.Jonky;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModVillagers {
    public static final ResourceKey<PoiType> BANKER_POI_KEY = poiKey("bankerpoi");
    public static final PoiType BANKER_POI = registerPoi("bankerpoi", Blocks.CHISELED_BOOKSHELF);
    public static final VillagerProfession BANKER = registerProfession("banker", BANKER_POI_KEY);

    private static VillagerProfession registerProfession(String name, ResourceKey<PoiType> type) {
        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, Identifier.fromNamespaceAndPath(Jonky.MOD_ID, name),
                new VillagerProfession(
                        Component.translatable("entity." + Jonky.MOD_ID + ".villager." + name),
                        poi -> poi.is(type),
                        poi -> poi.is(type),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        SoundEvents.VILLAGER_WORK_SHEPHERD,
                        new Int2ObjectOpenHashMap<>()));
    }

    private static PoiType registerPoi(String name, Block block) {
        return PoiHelper.register(Identifier.fromNamespaceAndPath(Jonky.MOD_ID, name), 1, 1, block);
    }

    private static ResourceKey<PoiType> poiKey(String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.fromNamespaceAndPath(Jonky.MOD_ID, name));
    }

    public static void registerVillagers() {
        Jonky.LOGGER.info("Registering Villagers " + Jonky.MOD_ID);
    }
}