package de.webtwob.mbma.api.capability.provider;

import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mbma.api.capability.APICapabilities.CAPABILITY_BLOCK_POS;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class BlockPosProvider implements ICapabilitySerializable {

    private IBlockPosProvider provider = CAPABILITY_BLOCK_POS.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY_BLOCK_POS;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(hasCapability(capability,facing)) {
            return (T) provider;
        }
        return null;
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
