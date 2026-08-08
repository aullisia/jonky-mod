package jonky.modid.block;

import jonky.modid.Jonky;
import jonky.modid.block.custom.ATM.ATMBlock;
import jonky.modid.block.custom.CopperRail.CopperRailBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static final Block ATM_BLOCK = registerBlock("atm_block",
            // Create block WITH registry key in settings
            settings -> new ATMBlock(settings.strength(4.0f, 6.0f).requiresCorrectToolForDrops()),
            true
    );

    public static final Block COPPER_RAIL = registerBlock("copper_rail",
            settings -> new CopperRailBlock(settings.strength(0.7F).noCollision()), // or customize as needed
            true
    );

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> blockFactory, boolean registerItem) {
        Identifier blockId = Identifier.fromNamespaceAndPath(Jonky.MOD_ID, name);
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, blockId);

        BlockBehaviour.Properties settings = BlockBehaviour.Properties.of().setId(blockKey);
        Block block = blockFactory.apply(settings);

        if (registerItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, blockId);
            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey));
            blockItem.registerBlocks(Item.BY_BLOCK, blockItem);
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static void registerModBlocks() {
        Jonky.LOGGER.info("Mod Blocks Initialised!");
    }
}