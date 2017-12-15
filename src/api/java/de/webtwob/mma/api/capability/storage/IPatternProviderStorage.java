package de.webtwob.mma.api.capability.storage;

import de.webtwob.mma.api.interfaces.capability.IPatternProvider;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 03. Dez. 2017.
 */
public class IPatternProviderStorage implements Capability.IStorage<IPatternProvider> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IPatternProvider> capability, IPatternProvider instance, EnumFacing side) {
        //the provider is not supposed  to store stuff
        return null;
    }

    @Override
    public void readNBT(Capability<IPatternProvider> capability, IPatternProvider instance, EnumFacing side, NBTBase nbt) {
        //we don't store stuff therefore we don't read stuff
    }
}
