package de.webtwob.mma.api.capability.storage;

import de.webtwob.mma.api.interfaces.tileentity.IMoveRequestProcessor;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class IMoveRequestProcessorStorage implements Capability.IStorage<IMoveRequestProcessor> {

    @Nullable
    @Override
    public NBTBase writeNBT(final Capability<IMoveRequestProcessor> capability, final IMoveRequestProcessor instance, final EnumFacing side) {
        //nothing to store
        return null;
    }

    @Override
    public void readNBT(final Capability<IMoveRequestProcessor> capability, final IMoveRequestProcessor instance, final EnumFacing side, final NBTBase nbt) {
        //we don't store something we don't read
    }
}
