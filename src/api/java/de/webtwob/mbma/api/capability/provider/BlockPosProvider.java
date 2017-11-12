package de.webtwob.mbma.api.capability.provider;

import de.webtwob.mbma.api.references.NBTKeys;
import de.webtwob.mbma.api.interfaces.capability.IBlockPosProvider;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mbma.api.capability.APICapabilities.CAPABILITY_BLOCK_POS;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class BlockPosProvider implements ICapabilitySerializable {
    
    private ItemStack item;
    private IBlockPosProvider provider = new IBlockPosProvider() {
        
        @Override
        public BlockPos getBlockPos() {
            NBTTagCompound compound = item.getTagCompound();
            if (compound == null) return null;
            int[] posA = compound.getIntArray(NBTKeys.LINK_SHARE_POS);
            if (posA.length >= 3) {
                return new BlockPos(posA[0], posA[1], posA[2]);
            }
            return null;
        }
        
        @Override
        public void setBlockPos(BlockPos pos) {
            NBTTagCompound compound = item.getTagCompound();
            if (compound == null) compound = new NBTTagCompound();
            if (pos != null) {
                compound.setIntArray(NBTKeys.LINK_SHARE_POS, new int[]{pos.getX(), pos.getY(), pos.getZ()});
            } else {
                compound.removeTag(NBTKeys.LINK_SHARE_POS);
            }
            item.setTagCompound(compound);
        }
    };
    
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
        return CAPABILITY_BLOCK_POS.getStorage().writeNBT(CAPABILITY_BLOCK_POS,
                provider, null);
    }
    
    @Override
    public void deserializeNBT(NBTBase nbt) {
        CAPABILITY_BLOCK_POS.getStorage().readNBT(CAPABILITY_BLOCK_POS, provider,
                null, nbt);
    }
}
