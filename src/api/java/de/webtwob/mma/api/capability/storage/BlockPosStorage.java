package de.webtwob.mma.api.capability.storage;

import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;
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
        return new NBTTagIntArray(pos == null ? new int[0] : new int[]{pos.getX(), pos.getY(), pos.getZ()});
    }

    @Override
    public void readNBT(
            Capability<IBlockPosProvider> capability, IBlockPosProvider instance, EnumFacing side, NBTBase nbt
    ) {
        if (nbt instanceof NBTTagIntArray) {
            int[] pos = ((NBTTagIntArray) nbt).getIntArray();
            instance.setBlockPos(pos.length >= 3 ? new BlockPos(pos[0], pos[1], pos[2]) : null);
        }
    }
}
