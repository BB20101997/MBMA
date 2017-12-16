package de.webtwob.mma.core.common.item;

import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mma.api.property.IsLinkedItemPropertyGetter;
import de.webtwob.mma.core.common.creativetab.CoreCreativeTab;
import de.webtwob.mma.core.common.references.MMAUnlocalizedNames;
import de.webtwob.mma.core.common.references.ResourceLocations;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by bennet on 17.03.17.
 */
public class LinkCardItem extends MMAItem {

    public LinkCardItem(ResourceLocation rl) {
        super(rl);
        setCreativeTab(CoreCreativeTab.MMATab);
        addPropertyOverride(ResourceLocations.Items.LINKED, IsLinkedItemPropertyGetter.INSTANCE);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (!player.isSneaking()) {
            return EnumActionResult.PASS;
        }
        if (!worldIn.isRemote) {
            IBlockPosProvider.setBlockPos(itemStack, pos);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!playerIn.isSneaking()){
            return new ActionResult<>(EnumActionResult.PASS,stack);
        }
        if (!worldIn.isRemote) {
            IBlockPosProvider.setBlockPos(stack, null);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nonnull
    @SuppressWarnings("StringConcatenationMissingWhitespace")
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (IBlockPosProvider.getBlockPos(stack) != null) {
            return getUnlocalizedName() + MMAUnlocalizedNames.LINKED_SUFFIX;
        } else {
            return getUnlocalizedName() + MMAUnlocalizedNames.UNLINKED_SUFFIX;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);
        BlockPos link = IBlockPosProvider.getBlockPos(stack);
        if (link != null) {
            tooltip.add(String.format("Linked to Block at: X:%d Y:%d Z:%d", link.getX(), link.getY(), link.getZ()));
        } else {
            tooltip.add("Not Linked");
        }
    }

    @Override
    public boolean getShareTag() {
        return true;
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
