package jonky.modid.block;

import jonky.modid.Jonky;
import jonky.modid.block.custom.ATM.ATMBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {
    public static final Block ATM_BLOCK = registerBlock("atm_block",
            // Create block WITH registry key in settings
            settings -> new ATMBlock(settings),
            true
    );

    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> blockFactory, boolean registerItem) {
        Identifier blockId = Identifier.of(Jonky.MOD_ID, name);
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, blockId);

        AbstractBlock.Settings settings = AbstractBlock.Settings.create().registryKey(blockKey);
        Block block = blockFactory.apply(settings);

        if (registerItem) {
            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, blockId);
            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
            Registry.register(Registries.ITEM, itemKey, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Jonky.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Jonky.LOGGER.info("Mod Blocks Initialised!");
    }
}
