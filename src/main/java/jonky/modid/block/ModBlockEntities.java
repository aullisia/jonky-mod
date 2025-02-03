package jonky.modid.block;

import jonky.modid.Jonky;
import jonky.modid.block.custom.ATM.ATMBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<ATMBlockEntity> ATM_BLOCK_ENTITY =
            registerBlockEntity("atm", ATMBlockEntity::new, ModBlocks.ATM_BLOCK);

    private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> factory,
            Block... blocks) {

        Identifier id = Identifier.of(Jonky.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id,
                FabricBlockEntityTypeBuilder.<T>create(factory, blocks).build());
    }

    public static void registerModBlockEntities() {
        Jonky.LOGGER.info("Mod Block Entities Initialized!");
    }

    public static void registerModBlocks() {
        Jonky.LOGGER.info("Mod Block Entities initialised!");
    }
}
