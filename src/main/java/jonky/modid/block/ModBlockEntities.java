package jonky.modid.block;

import jonky.modid.Jonky;
import jonky.modid.block.custom.ATM.ATMBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class ModBlockEntities {
    public static final BlockEntityType<ATMBlockEntity> ATM_BLOCK_ENTITY =
            registerBlockEntity("atm", ATMBlockEntity::new, ModBlocks.ATM_BLOCK);

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(
            String name,
            BlockEntityType.BlockEntitySupplier<? extends T> factory,
            Block... blocks) {

        Identifier id = Identifier.fromNamespaceAndPath(Jonky.MOD_ID, name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id,
                new BlockEntityType<>(factory, Set.of(blocks)));
    }

    public static void registerModBlockEntities() {
        Jonky.LOGGER.info("Mod Block Entities Initialized!");
    }
}