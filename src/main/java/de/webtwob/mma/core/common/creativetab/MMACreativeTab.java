package de.webtwob.mma.core.common.creativetab;

import de.webtwob.mma.core.common.registration.Blocks;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by bennet on 17.03.17.
 */
public class MMACreativeTab extends CreativeTabs {


    public static final CreativeTabs MMATab = new MMACreativeTab();

    private MMACreativeTab() {
        super("mma");
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Blocks.STORAGE_INTERFACE);
    }
}
