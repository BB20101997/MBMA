package de.webtwob.mma.api.capability.storage;

import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;

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
        BlockPos pos = instance.getBlockPos();
        return pos != null ? NBTUtil.createPosTag(instance.getBlockPos()) : new NBTTagCompound();
    }

    @Override
    public void readNBT(Capability<IBlockPosProvider> capability, IBlockPosProvider instance, EnumFacing side, NBTBase nbt) {
        if (nbt instanceof NBTTagCompound && !nbt.hasNoTags()) {
            instance.setBlockPos(NBTUtil.getPosFromTag((NBTTagCompound) nbt));
        }
        instance.setBlockPos(null);
    }
}
