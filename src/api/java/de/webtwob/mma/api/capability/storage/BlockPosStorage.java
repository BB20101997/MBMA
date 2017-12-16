package de.webtwob.mma.api.capability.storage;

import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mma.api.references.NBTKeys;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class BlockPosStorage implements Capability.IStorage<IBlockPosProvider> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IBlockPosProvider> capability, IBlockPosProvider instance, EnumFacing side) {
        BlockPos       pos      = instance.getBlockPos();
        NBTTagCompound compound = new NBTTagCompound();
        if (pos != null) {
            compound.setTag(NBTKeys.LINK_SHARE_POS, NBTUtil.createPosTag(pos));
        }
        return compound;
    }

    @Override
    public void readNBT(Capability<IBlockPosProvider> capability, IBlockPosProvider instance, EnumFacing side, NBTBase nbt) {
        if (nbt instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            if (compound.hasKey(NBTKeys.LINK_SHARE_POS)){
                instance.setBlockPos(NBTUtil.getPosFromTag(compound.getCompoundTag(NBTKeys.LINK_SHARE_POS)));
            }else{
                instance.setBlockPos(null);
            }
        }
    }
}
