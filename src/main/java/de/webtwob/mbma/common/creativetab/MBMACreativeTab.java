package de.webtwob.mbma.common.creativetab;

import de.webtwob.mbma.common.block.MBMABlockList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by bennet on 17.03.17.
 */
public class MBMACreativeTab extends CreativeTabs {

    public static final CreativeTabs MBMATab = new MBMACreativeTab();

    private MBMACreativeTab() {
        super("mbma");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(MBMABlockList.PSI_BLOCK);
    }
}
