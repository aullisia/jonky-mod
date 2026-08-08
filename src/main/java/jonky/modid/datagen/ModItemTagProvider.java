package jonky.modid.datagen;

import jonky.modid.item.ModItems;
import jonky.modid.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(ModTags.Items.HEAVY_CORE_REPAIR).add(BuiltInRegistries.ITEM.getResourceKey(Items.HEAVY_CORE).orElseThrow());

        builder(ItemTags.SWORDS).add(BuiltInRegistries.ITEM.getResourceKey(ModItems.HEAVY_GREATSWORD).orElseThrow());
        builder(ItemTags.PICKAXES).add(BuiltInRegistries.ITEM.getResourceKey(ModItems.HEAVY_PICKAXE).orElseThrow());
        builder(ItemTags.SHOVELS).add(BuiltInRegistries.ITEM.getResourceKey(ModItems.HEAVY_SHOVEL).orElseThrow());
        builder(ItemTags.AXES).add(BuiltInRegistries.ITEM.getResourceKey(ModItems.HEAVY_AXE).orElseThrow());
        builder(ItemTags.HOES).add(BuiltInRegistries.ITEM.getResourceKey(ModItems.HEAVY_HOE).orElseThrow());
    }
}