package de.webtwob.mbma.common.item;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import de.webtwob.mbma.api.property.IsLinkedItemPropertyGetter;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.references.MBMAResources;
import de.webtwob.mbma.common.references.MBMAUnlocalizedNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by bennet on 17.03.17.
 */
public class LinkCardItem extends Item {

    public LinkCardItem() {
        super();
        setCreativeTab(MBMACreativeTab.MBMATab);
        addPropertyOverride(MBMAResources.LINKED, IsLinkedItemPropertyGetter.INSTANCE);
    }

    private BlockPos getLinked(ItemStack stack) {
        IBlockPosProvider ibpp;
        if ((ibpp = stack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null)) != null) {
            return ibpp.getBlockPos();
        }

        return null;
    }

    private void setLink(ItemStack stack, BlockPos pos, World world) {
        if (!world.isRemote) {
            IBlockPosProvider posProvider;
            if ((posProvider = stack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null)) != null) {
                posProvider.setBlockPos(pos);
            }
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            if (!worldIn.isRemote) {
                setLink(itemStack, pos, worldIn);
            }
            return EnumActionResult.SUCCESS;

        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn.isSneaking()) {
            if (!worldIn.isRemote) {
                setLink(stack, null, worldIn);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);

        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nonnull
    @SuppressWarnings("StringConcatenationMissingWhitespace")
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (getLinked(stack) != null) {
            return getUnlocalizedName() + MBMAUnlocalizedNames.LINKED_SUFIX;
        } else {
            return getUnlocalizedName() + MBMAUnlocalizedNames.UNLINKED_SUFIX;
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        BlockPos link = getLinked(stack);
        if (link != null) {
            tooltip.add(String.format("Linked to Block at: X:%d Y:%d Z:%d", link.getX(), link.getY(), link.getZ()));
        }
    }

    @Override
    public boolean updateItemStackNBT(NBTTagCompound nbt) {
        return true;
    }


    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 8;
    }
}
