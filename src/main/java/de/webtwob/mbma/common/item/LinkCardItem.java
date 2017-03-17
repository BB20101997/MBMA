package de.webtwob.mbma.common.item;

import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import de.webtwob.mbma.common.references.UnlocalizedNames;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by bennet on 17.03.17.
 */
public class LinkCardItem extends Item {

    public LinkCardItem() {
        super();
        setUnlocalizedName(UnlocalizedNames.LINKCARD_NAME);
        setCreativeTab(CreativeTabs.MISC);
        setCreativeTab(MBMACreativeTab.MBMATab);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null && compound.hasKey(MBMA_NBTKeys.LINKCARD_LINK, 11)) {
            return UnlocalizedNames.ITEM_PREFIX+UnlocalizedNames.LINKCARD_NAME+UnlocalizedNames.LINKED_SUFIX;
        } else {
            return UnlocalizedNames.ITEM_PREFIX+UnlocalizedNames.LINKCARD_NAME+UnlocalizedNames.UNLINKED_SUFIX;
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        if (player.isSneaking()) {
            nbt.setIntArray(MBMA_NBTKeys.LINKCARD_LINK, new int[]{pos.getX(), pos.getY(), pos.getZ()});
            itemStack.setTagCompound(nbt);
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
