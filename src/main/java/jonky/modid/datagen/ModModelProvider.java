package jonky.modid.datagen;

import jonky.modid.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.List;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    private static final List<Item> HEAVY_TOOLS = List.of(
            ModItems.HEAVY_PICKAXE,
            ModItems.HEAVY_SHOVEL,
            ModItems.HEAVY_AXE,
            ModItems.HEAVY_HOE,
            ModItems.HEAVY_GREATSWORD
    );

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for (Item item : HEAVY_TOOLS) {
            Identifier modelId = ModelTemplates.FLAT_HANDHELD_ITEM.create(
                    ModelLocationUtils.getModelLocation(item),
                    TextureMapping.layer0(item),
                    itemModelGenerator.modelOutput
            );
            itemModelGenerator.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
        }
    }
}