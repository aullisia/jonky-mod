package jonky.modid.villager;

import com.google.common.collect.ImmutableSet;
import jonky.modid.Jonky;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Collections;

public class ModVillagers {
    public static final RegistryKey<PointOfInterestType> BANKER_POI_KEY = poiKey("bankerpoi");
    public static final PointOfInterestType BANKER_POI = registerPoi("bankerpoi", Blocks.CHISELED_BOOKSHELF);
    public static final VillagerProfession BANKER = registerProfession("banker", BANKER_POI_KEY);

    private static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type) {
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(Jonky.MOD_ID, name),
                new VillagerProfession(name, entry -> entry.matchesKey(type), entry -> entry.matchesKey(type),
                        ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_SHEPHERD));
    }

    private static PointOfInterestType registerPoi(String name, Block block) {
        return PointOfInterestHelper.register(Identifier.of(Jonky.MOD_ID, name), 1, 1, block);
    }

    private static RegistryKey<PointOfInterestType> poiKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(Jonky.MOD_ID, name));
    }

    public static void registerVillagers() {
        Jonky.LOGGER.info("Registering Villagers " + Jonky.MOD_ID);
    }
}
