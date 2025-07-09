package jonky.modid.item;

import jonky.modid.Jonky;
import jonky.modid.block.ModBlocks;
import jonky.modid.util.BanknoteUtils;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup JONKY_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Jonky.MOD_ID, "jonky_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.BANKNOTE))
                    .displayName(Text.translatable("itemgroup.jonky.jonky_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(BanknoteUtils.createBanknoteStack(5, 1));
                        entries.add(BanknoteUtils.createBanknoteStack(10, 1));
                        entries.add(BanknoteUtils.createBanknoteStack(20, 1));
                        entries.add(BanknoteUtils.createBanknoteStack(50, 1));
                        entries.add(BanknoteUtils.createBanknoteStack(100, 1));
                        entries.add(BanknoteUtils.createBanknoteStack(200, 1));
                        entries.add(BanknoteUtils.createBanknoteStack(500, 1));

                        entries.add(ModItems.WRENCH);
                        entries.add(ModItems.HEAVY_SHIELD);
                        entries.add(ModItems.HEAVY_GREATSWORD);
                        entries.add(ModItems.HEAVY_PICKAXE);
                        entries.add(ModItems.HEAVY_SHOVEL);
                        entries.add(ModItems.HEAVY_AXE);
                        entries.add(ModItems.HEAVY_HOE);
                        entries.add(ModItems.HEAVY_UPGRADE);

                        entries.add(ModBlocks.ATM_BLOCK);
                        entries.add(ModBlocks.COPPER_RAIL);

                    }).build());

    public static void registerItemGroups() {
    }
}
