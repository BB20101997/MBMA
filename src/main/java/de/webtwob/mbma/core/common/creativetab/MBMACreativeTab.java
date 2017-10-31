package de.webtwob.mbma.core.common.creativetab;

import de.webtwob.mbma.core.common.registration.MBMABlockList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by bennet on 17.03.17.
 */
public class MBMACreativeTab extends CreativeTabs {


    public static final CreativeTabs MBMATab = new MBMACreativeTab();

    private MBMACreativeTab() {
        super("mbma");
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(MBMABlockList.STORAGE_INTERFACE);
    }
}
