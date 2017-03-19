package de.webtwob.mbma.api.capability.provider;

import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mbma.api.capability.APICapabilities.CAPABILITY_CRAFTING_REQUEST;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class CraftingRecipeProvider implements ICapabilitySerializable {

    private ICraftingRequest provider = CAPABILITY_CRAFTING_REQUEST.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY_CRAFTING_REQUEST;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(hasCapability(capability,facing)){
            return (T) provider;
        }
        return null;
    }

    @Override
    public NBTBase serializeNBT() {
        return CAPABILITY_CRAFTING_REQUEST.getStorage().writeNBT(CAPABILITY_CRAFTING_REQUEST, provider, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        CAPABILITY_CRAFTING_REQUEST.getStorage().readNBT(CAPABILITY_CRAFTING_REQUEST, provider, null, nbt);
    }
}
