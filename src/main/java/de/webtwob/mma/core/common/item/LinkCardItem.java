package de.webtwob.mma.core.common.item;

import de.webtwob.mma.api.capability.APICapabilities;
import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mma.api.property.IsLinkedItemPropertyGetter;
import de.webtwob.mma.core.common.creativetab.MMACreativeTab;
import de.webtwob.mma.core.common.references.MMAUnlocalizedNames;
import de.webtwob.mma.core.common.references.ResourceLocations;

import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by bennet on 17.03.17.
 */
public class LinkCardItem extends Item {

    public LinkCardItem() {
        super();
        setCreativeTab(MMACreativeTab.MMATab);
        addPropertyOverride(ResourceLocations.Items.LINKED, IsLinkedItemPropertyGetter.INSTANCE);
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
            return getUnlocalizedName() + MMAUnlocalizedNames.LINKED_SUFIX;
        } else {
            return getUnlocalizedName() + MMAUnlocalizedNames.UNLINKED_SUFIX;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);
        BlockPos link = getLinked(stack);
        if (link != null) {
            tooltip.add(String.format("Linked to Block at: X:%d Y:%d Z:%d", link.getX(), link.getY(), link.getZ()));
        } else {
            tooltip.add("Not Linked");
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
