package jonky.modid.item;

import com.google.common.base.Suppliers;
import jonky.modid.util.ModTags;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Supplier;

public class ModToolMaterials {
    public static final ToolMaterial HEAVY_CORE = new ToolMaterial(ModTags.Blocks.INCORRECT_FOR_HEAVY_CORE_TOOL, 1500, 7.0f,
            2.0f, 22, ModTags.Items.HEAVY_CORE_REPAIR);
}