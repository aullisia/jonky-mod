package jonky.modid.util;

import jonky.modid.Jonky;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_HEAVY_CORE_TOOL = createTag("needs_heavy_core_tool");
        public static final TagKey<Block> INCORRECT_FOR_HEAVY_CORE_TOOL = createTag("incorrect_for_heavy_core_tool");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Jonky.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> HEAVY_CORE_REPAIR = createTag("heavy_core_repair");
        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Jonky.MOD_ID, name));
        }
    }
}