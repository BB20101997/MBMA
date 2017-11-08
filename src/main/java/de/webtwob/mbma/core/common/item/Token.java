package de.webtwob.mbma.core.common.item;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.core.MBMA_CORE;
import de.webtwob.mbma.core.common.creativetab.MBMACreativeTab;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class Token extends Item {
    
    public Token() {
        setCreativeTab(MBMACreativeTab.MBMATab);
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
        if ((icr = stack.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
            if (!icr.isCompleted()) {
                tooltip.add(String.format("Request for %d %s", icr.getQuantity(), icr.getRequest().getDisplayName()));
            }
        }
    }
    
    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            BlockPos pos = playerIn.getPosition();
            playerIn.openGui(
                    MBMA_CORE.INSTANCE, handIn.ordinal() + 1, worldIn, pos.getX(), pos.getY(),
                    pos.getZ()
            );
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
    
    @Nullable
    @Override
    public IItemPropertyGetter getPropertyGetter(@Nonnull ResourceLocation key) {
        return super.getPropertyGetter(key);
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
    public boolean updateItemStackNBT(final NBTTagCompound p_updateItemStackNBT_1_) {
        return true;
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 16;
    }
}
