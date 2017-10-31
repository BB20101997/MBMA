package de.webtwob.mbma.api.capability.storage;

import de.webtwob.mbma.api.interfaces.capability.ICraftingRecipe;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Created by bennet on 03.04.17.
 */
public class CraftingRecipeStorage implements Capability.IStorage<ICraftingRecipe> {

    @Nullable
    @Override
    public NBTBase writeNBT(
            final Capability<ICraftingRecipe> capability, final ICraftingRecipe iCraftingRecipe,
            final EnumFacing enumFacing
    ) {
        return iCraftingRecipe.serializeNBT();
    }

    @Override
    public void readNBT(
            final Capability<ICraftingRecipe> capability, final ICraftingRecipe iCraftingRecipe,
            final EnumFacing enumFacing, final NBTBase nbtBase
    ) {
        if (nbtBase instanceof NBTTagCompound) {
            iCraftingRecipe.deserializeNBT((NBTTagCompound) nbtBase);
        }
    }
}
