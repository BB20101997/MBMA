package de.webtwob.mma.api.capability.storage;

import de.webtwob.mma.api.interfaces.capability.ICraftingRequestProvider;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 03. Dez. 2017.
 */
public class ICraftingRequestProviderIStorage implements Capability.IStorage<ICraftingRequestProvider> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ICraftingRequestProvider> capability, ICraftingRequestProvider instance, EnumFacing side) {
        //by default we don't store stuff in the Provider
        return null;
    }
    
    @Override
    public void readNBT(Capability<ICraftingRequestProvider> capability, ICraftingRequestProvider instance, EnumFacing side, NBTBase nbt) {
        //by default we won't don't store stuff so we can't read stuff
    }
}
