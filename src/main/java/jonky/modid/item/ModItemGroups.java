package jonky.modid.item;

import jonky.modid.Jonky;
import jonky.modid.block.ModBlocks;
import jonky.modid.util.BanknoteUtils;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    public static final ResourceKey<CreativeModeTab> JONKY_ITEMS_GROUP_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Jonky.MOD_ID, "jonky_items"));

    public static final CreativeModeTab JONKY_ITEMS_GROUP = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            JONKY_ITEMS_GROUP_KEY,
            FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.BANKNOTE))
                    .title(Component.translatable("itemgroup.jonky.jonky_items"))
                    .displayItems((params, output) -> {
                        output.accept(BanknoteUtils.createBanknoteStack(5, 1));
                        output.accept(BanknoteUtils.createBanknoteStack(10, 1));
                        output.accept(BanknoteUtils.createBanknoteStack(20, 1));
                        output.accept(BanknoteUtils.createBanknoteStack(50, 1));
                        output.accept(BanknoteUtils.createBanknoteStack(100, 1));
                        output.accept(BanknoteUtils.createBanknoteStack(200, 1));
                        output.accept(BanknoteUtils.createBanknoteStack(500, 1));

                        output.accept(ModItems.WRENCH);
                        output.accept(ModItems.HEAVY_SHIELD);
                        output.accept(ModItems.HEAVY_GREATSWORD);
                        output.accept(ModItems.HEAVY_PICKAXE);
                        output.accept(ModItems.HEAVY_SHOVEL);
                        output.accept(ModItems.HEAVY_AXE);
                        output.accept(ModItems.HEAVY_HOE);
                        output.accept(ModItems.HEAVY_UPGRADE);

                        output.accept(ModBlocks.ATM_BLOCK);
                        output.accept(ModBlocks.COPPER_RAIL);

                    }).build());

    public static void registerItemGroups() {
    }
}