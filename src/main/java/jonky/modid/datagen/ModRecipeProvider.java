package jonky.modid.datagen;

import jonky.modid.Jonky;
import jonky.modid.block.ModBlocks;
import jonky.modid.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
//        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BANKNOTE)
//                .pattern("RRR")
//                .pattern("RRR")
//                .pattern("RRR")
//                .input('R', ModItems.BANKNOTE)
//                .criterion(hasItem(ModItems.BANKNOTE), conditionsFromItem(ModItems.BANKNOTE))
//                .offerTo(exporter);
    }
}
