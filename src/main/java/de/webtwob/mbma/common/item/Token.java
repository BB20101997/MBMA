package de.webtwob.mbma.common.item;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        NBTTagCompound compound = stack.getTagCompound();

        ItemStack request = null;

        if (compound != null && compound.hasKey(MBMA_NBTKeys.TOKEN_SHARE_REQUEST)) {
            request = new ItemStack(compound.getCompoundTag(MBMA_NBTKeys.TOKEN_SHARE_REQUEST));
        } else {
            ICraftingRequest icr;
            if ((icr = stack.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
                request = icr.getRequest();
            }
        }

        if (request != null && !request.isEmpty()) {
            tooltip.add(String.format("Request for %d %s", request.getCount(), request.getDisplayName()));
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            BlockPos pos = playerIn.getPosition();
            playerIn.openGui(MultiblockMaschineAutomation.INSTANCE, handIn.ordinal()+1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void getSubItems(@Nonnull Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        super.getSubItems(itemIn, tab, subItems);
        ItemStack stack = new ItemStack(this);
        ICraftingRequest icr;
        if ((icr = stack.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
            icr.setRequest(new ItemStack(Blocks.DIRT, 1));
        }
        subItems.add(stack);
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        NBTTagCompound compound = super.getNBTShareTag(stack);
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        ICraftingRequest icr;
        if ((icr = stack.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
            compound.setTag(MBMA_NBTKeys.TOKEN_SHARE_REQUEST, icr.getRequest().serializeNBT());
        }
        return compound;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        ICraftingRequest iCraftingRequest;
        if ((iCraftingRequest = stack.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
            if (iCraftingRequest.getRequest() == ItemStack.EMPTY) {
                return 64;
            } else {
                return 1;
            }
        }
        return super.getItemStackLimit(stack);
    }
}
