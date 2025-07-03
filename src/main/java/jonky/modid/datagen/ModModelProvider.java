package jonky.modid.datagen;

import jonky.modid.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // itemModelGenerator.register(ModItems.HEAVY_GREATSWORD, Models.HANDHELD);
        itemModelGenerator.register(ModItems.HEAVY_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.HEAVY_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(ModItems.HEAVY_AXE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.HEAVY_HOE, Models.HANDHELD);
    }
}
