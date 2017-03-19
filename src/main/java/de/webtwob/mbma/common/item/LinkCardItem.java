package de.webtwob.mbma.common.item;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.references.MBMAUnlocalizedNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        setCreativeTab(MBMACreativeTab.MBMATab);
    }

    private BlockPos getLinked(ItemStack stack) {
        if(stack.hasCapability(APICapabilities.CAPABILITY_BLOCK_POS, null)) {
            return stack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null).getBlockPos();
        }
        return null;
    }

    private void setLink(ItemStack stack, BlockPos pos) {
        if(stack.hasCapability(APICapabilities.CAPABILITY_BLOCK_POS, null)) {
            stack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null).setBlockPos(pos);
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(player.isSneaking()) {
            setLink(itemStack, pos);
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            setLink(stack, null);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if(getLinked(stack) != null) {
            return getUnlocalizedName()+ MBMAUnlocalizedNames.LINKED_SUFIX;
        } else {
            return getUnlocalizedName() + MBMAUnlocalizedNames.UNLINKED_SUFIX;
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        BlockPos link = getLinked(stack);
        if(link != null) {
            tooltip.add(String.format("Linked to Block at: X:%d Y:%d Z:%d", link.getX(), link.getY(), link.getZ()));
        }
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 16;
    }
}
