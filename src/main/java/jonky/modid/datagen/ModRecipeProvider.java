package jonky.modid.datagen;

import jonky.modid.block.ModBlocks;
import jonky.modid.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    private static ResourceKey<Recipe<?>> smithingRecipeId(Item result) {
        return ResourceKey.create(Registries.RECIPE, BuiltInRegistries.ITEM.getKey(result));
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipeOutput) {
        return new RecipeProvider(registries, recipeOutput) {
            private void createHeavyCoreSmithingRecipe(Item addition, Item result) {
                SmithingTransformRecipeBuilder.smithing(
                                Ingredient.of(ModItems.HEAVY_UPGRADE),
                                Ingredient.of(addition),
                                Ingredient.of(Items.HEAVY_CORE),
                                RecipeCategory.COMBAT,
                                result
                        )
                        .unlocks("has_heavy_core", has(Items.HEAVY_CORE))
                        .save(output, smithingRecipeId(result));
            }

            private void createCopperRailRecipe() {
                Item copperBulb = BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace("copper_bulb"));
                shaped(RecipeCategory.TRANSPORTATION, ModBlocks.COPPER_RAIL, 6)
                        .define('g', Items.GOLD_INGOT)
                        .define('/', Items.STICK)
                        .define('W', copperBulb)
                        .pattern("g g")
                        .pattern("g/g")
                        .pattern("gWg")
                        .unlockedBy("has_copper_bulb", has(copperBulb))
                        .save(output);
            }

            @Override
            public void buildRecipes() {
                createHeavyCoreSmithingRecipe(Items.SHIELD, ModItems.HEAVY_SHIELD);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_AXE, ModItems.HEAVY_AXE);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_SWORD, ModItems.HEAVY_GREATSWORD);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_SHOVEL, ModItems.HEAVY_SHOVEL);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_HOE, ModItems.HEAVY_HOE);
                createHeavyCoreSmithingRecipe(Items.NETHERITE_PICKAXE, ModItems.HEAVY_PICKAXE);

                createCopperRailRecipe();
            }
        };
    }

    @Override
    public String getName() {
        return "JonkyMod Recipes";
    }
}