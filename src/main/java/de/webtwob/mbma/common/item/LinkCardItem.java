package de.webtwob.mbma.common.item;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.references.MBMAUnlocalizedNames;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by bennet on 17.03.17.
 */
public class LinkCardItem extends Item {

    public LinkCardItem() {
        super();
        setCreativeTab(MBMACreativeTab.MBMATab);
    }

    private BlockPos getLinked(ItemStack stack, boolean client_side) {
        if (client_side) {
            NBTTagCompound compound = stack.getTagCompound();
            if (compound != null && compound.hasKey(MBMA_NBTKeys.LINK_SHARE_POS)) {
                int[] pos = compound.getIntArray(MBMA_NBTKeys.LINK_SHARE_POS);
                return new BlockPos(pos[0], pos[1], pos[2]);
            }
        }
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
        if (getLinked(stack, true) != null) {
            return getUnlocalizedName() + MBMAUnlocalizedNames.LINKED_SUFIX;
        } else {
            return getUnlocalizedName() + MBMAUnlocalizedNames.UNLINKED_SUFIX;
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        BlockPos link = getLinked(stack, true);
        if (link != null) {
            tooltip.add(String.format("Linked to Block at: X:%d Y:%d Z:%d", link.getX(), link.getY(), link.getZ()));
        }
    }


    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        NBTTagCompound compound = super.getNBTShareTag(stack);
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        IBlockPosProvider posProvider;
        if ((posProvider = stack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null)) != null) {
            BlockPos pos = posProvider.getBlockPos();
            if (pos != null) {
                compound.setIntArray(MBMA_NBTKeys.LINK_SHARE_POS, new int[]{pos.getX(), pos.getY(), pos.getZ()});
            } else {
                compound.removeTag(MBMA_NBTKeys.LINK_SHARE_POS);
            }
        }
        return compound;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
}
