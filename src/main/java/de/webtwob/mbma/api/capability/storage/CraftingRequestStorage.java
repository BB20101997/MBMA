package de.webtwob.mbma.api.capability.storage;

import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class CraftingRequestStorage implements Capability.IStorage<ICraftingRequest> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ICraftingRequest> capability, ICraftingRequest instance, EnumFacing side) {
        return instance.getRequest().serializeNBT();
    }

    @Override
    public void readNBT(Capability<ICraftingRequest> capability, ICraftingRequest instance, EnumFacing side, NBTBase nbt) {
        instance.setRequest(nbt instanceof NBTTagCompound ? new ItemStack((NBTTagCompound) nbt) : ItemStack.EMPTY);
    }
}
