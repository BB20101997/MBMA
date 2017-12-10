package de.webtwob.mma.core.common.item;

import de.webtwob.mma.api.capability.APICapabilities;
import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.core.MMACore;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class Token extends MMAItem {

    public Token(ResourceLocation rl) {
        super(rl);
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, world, tooltip, advanced);

        ICraftingRequest icr;
        if ((icr = stack.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null && !icr.isCompleted()) {
            tooltip.add(String.format("Request for %d %s", icr.getQuantity(), icr.getRequest().getDisplayName()));
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            BlockPos pos = playerIn.getPosition();
            playerIn.openGui(
                    MMACore.INSTANCE, handIn.ordinal() + 1, worldIn, pos.getX(), pos.getY(),
                    pos.getZ()
            );
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab)) {
            subItems.add(new ItemStack(this));
            ItemStack stack = new ItemStack(this);
            ICraftingRequest icr;
            if ((icr = stack.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
                icr.setRequest(new ItemStack(Blocks.DIRT));
                icr.setQuantity(1);
            }
            subItems.add(stack);
        }
    }

    @Override
    public boolean updateItemStackNBT(final NBTTagCompound nbtTagCompound) {
        return true;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 16;
    }
}
