package jonky.modid.datagen;

import jonky.modid.item.ModItems;
import jonky.modid.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getTagBuilder(ModTags.Items.HEAVY_CORE_REPAIR).add(Registries.ITEM.getId(Items.HEAVY_CORE));

        getTagBuilder(ItemTags.SWORDS).add(Registries.ITEM.getId(ModItems.HEAVY_GREATSWORD));
        getTagBuilder(ItemTags.PICKAXES).add(Registries.ITEM.getId(ModItems.HEAVY_PICKAXE));
        getTagBuilder(ItemTags.SHOVELS).add(Registries.ITEM.getId(ModItems.HEAVY_SHOVEL));
        getTagBuilder(ItemTags.AXES).add(Registries.ITEM.getId(ModItems.HEAVY_AXE));
        getTagBuilder(ItemTags.HOES).add(Registries.ITEM.getId(ModItems.HEAVY_HOE));
    }
}
