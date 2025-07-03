package jonky.modid.util;

import jonky.modid.Jonky;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_HEAVY_CORE_TOOL = createTag("needs_heavy_core_tool");
        public static final TagKey<Block> INCORRECT_FOR_HEAVY_CORE_TOOL = createTag("incorrect_for_heavy_core_tool");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(Jonky.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> HEAVY_CORE_REPAIR = createTag("heavy_core_repair");
        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(Jonky.MOD_ID, name));
        }
    }
}
