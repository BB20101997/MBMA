package de.webtwob.mbma.common.item;

import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import de.webtwob.mbma.common.references.UnlocalizedNames;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

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
        if (getLinked(stack)!=null) {
            return UnlocalizedNames.ITEM_PREFIX+UnlocalizedNames.LINKCARD_NAME+UnlocalizedNames.LINKED_SUFIX;
        } else {
            return UnlocalizedNames.ITEM_PREFIX+UnlocalizedNames.LINKCARD_NAME+UnlocalizedNames.UNLINKED_SUFIX;
        }
    }

    private int[] getLinked(ItemStack stack){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound!=null && compound.hasKey(MBMA_NBTKeys.LINKCARD_LINK,11)){
            return compound.getIntArray(MBMA_NBTKeys.LINKCARD_LINK);
        }else{
            return null;
        }
    }

    private void setLink(ItemStack stack, BlockPos pos){
        NBTTagCompound compound = stack.getTagCompound();
        if(compound==null){
            compound = new NBTTagCompound();
        }
        if(pos!=null){
           compound.setIntArray(MBMA_NBTKeys.LINKCARD_LINK,new int[]{pos.getX(),pos.getY(),pos.getZ()});
        }else{
            compound.removeTag(MBMA_NBTKeys.LINKCARD_LINK);
        }
        stack.setTagCompound(compound);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        int[] link = getLinked(stack);
        if(link!=null){
            tooltip.add(String.format("Linked to Block at: X:%d Y:%d Z:%d",link[0],link[1],link[2] ));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()){
            setLink(stack,null);
            return new ActionResult<>(EnumActionResult.SUCCESS,stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            setLink(itemStack,pos);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
