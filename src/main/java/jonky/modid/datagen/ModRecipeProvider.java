package jonky.modid.datagen;

import jonky.modid.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.data.recipe.VanillaRecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }


    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {

            private void createHeavyCoreSmithingRecipe(Item addition, Item result) {
                SmithingTransformRecipeJsonBuilder.create(
                                Ingredient.ofItems(ModItems.HEAVY_UPGRADE), // template
                                Ingredient.ofItems(addition),               // base
                                Ingredient.ofItems(Items.HEAVY_CORE),       // addition
                                RecipeCategory.COMBAT,                      // category
                                result                                      // result
                        ).criterion("has_heavy_core", conditionsFromItem(Items.HEAVY_CORE))
                        .offerTo(exporter, String.valueOf(Registries.ITEM.getId(result)));
            }
            @Override
            public void generate() {
                createHeavyCoreSmithingRecipe(Items.SHIELD ,ModItems.HEAVY_SHIELD);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_AXE ,ModItems.HEAVY_AXE);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_SWORD ,ModItems.HEAVY_GREATSWORD);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_SHOVEL ,ModItems.HEAVY_SHOVEL);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_HOE ,ModItems.HEAVY_HOE);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_PICKAXE ,ModItems.HEAVY_PICKAXE);

//                SmithingTransformRecipeJsonBuilder.create(
//                                Ingredient.ofItems(ModItems.HEAVY_UPGRADE), // template
//                                Ingredient.ofItems(Items.SHIELD),                             // base
//                                Ingredient.ofItems(Items.HEAVY_CORE),                      // addition
//                                RecipeCategory.COMBAT,                                        // category
//                                ModItems.HEAVY_SHIELD                                         // result
//                        ).criterion("has_heavy_core", conditionsFromItem(Items.HEAVY_CORE))
//                        .offerTo(exporter, String.valueOf(Registries.ITEM.getId(ModItems.HEAVY_SHIELD)));
            }
        };
    }

    @Override
    public String getName() {
        return "JonkyMod Recipes";
    }
}
