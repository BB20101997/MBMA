package de.webtwob.mbma.api.capability.storage;

import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;

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

    public static final String REQ = "MBMAAPI:ICRREQUEST";
    public static final String QUANT = "MBMAAPI:ICRQUANTITY";

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ICraftingRequest> capability, ICraftingRequest instance, EnumFacing side) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger(QUANT, instance.getQuantity());
        compound.setTag(REQ, instance.getRequest().serializeNBT());
        return compound;
    }

    @Override
    public void readNBT(Capability<ICraftingRequest> capability, ICraftingRequest instance, EnumFacing side, NBTBase nbt) {
        if (nbt instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.setRequest(new ItemStack(compound.getCompoundTag(REQ)));
            instance.setQuantity(compound.getInteger(QUANT));
        }
    }
}
