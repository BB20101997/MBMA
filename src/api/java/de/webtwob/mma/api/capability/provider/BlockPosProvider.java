package de.webtwob.mma.api.capability.provider;

import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mma.api.references.NBTKeys;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mma.api.capability.APICapabilities.CAPABILITY_BLOCK_POS;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class BlockPosProvider implements ICapabilitySerializable {

    private ItemStack item;
    private IBlockPosProvider provider = new IBlockPosProvider() {

        @Override
        public BlockPos getBlockPos() {
            NBTTagCompound compound = item.getTagCompound();
            if (compound == null || !compound.hasKey(NBTKeys.LINK_SHARE_POS, Constants.NBT.TAG_COMPOUND)) {
                return null;
            } else {
                return NBTUtil.getPosFromTag(compound.getCompoundTag(NBTKeys.LINK_SHARE_POS));
            }
        }

        @Override
        public void setBlockPos(final BlockPos pos) {
            NBTTagCompound compound = item.getTagCompound();
            if (compound == null) {
                compound = new NBTTagCompound();
            }
            if (pos != null) {
                compound.setTag(NBTKeys.LINK_SHARE_POS, NBTUtil.createPosTag(pos));
            }else {
                compound.removeTag(NBTKeys.LINK_SHARE_POS);
            }
            item.setTagCompound(compound);
        }
    };

    /**
     * @param stack the ItemStack for which to provide an instance of IBlockPosProvider
     */
    public BlockPosProvider(ItemStack stack) {
        item = stack;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY_BLOCK_POS;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? (T) provider : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return provider.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        provider.deserializeNBT(nbt);
    }
}
