package de.webtwob.mma.api.capability.provider;

import de.webtwob.mma.api.capability.APICapabilities;
import de.webtwob.mma.api.interfaces.capability.ICraftingRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by bennet on 03.04.17.
 */
public class CraftingRecipeProvider implements ICapabilitySerializable {

    private ICraftingRecipe provided = APICapabilities.CAPABILITY_CRAFTING_RECIPE.getDefaultInstance();

    /**
     * @param stack the ItemStack for which to provide an instance of ICraftingRecipe
     */
    public CraftingRecipeProvider(final ItemStack stack) {
    }

    @Override
    public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing enumFacing) {
        return capability == APICapabilities.CAPABILITY_CRAFTING_RECIPE;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing enumFacing) {
        return hasCapability(capability, enumFacing) ? (T) provided : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return provided.serializeNBT();
    }

    @Override
    public void deserializeNBT(final NBTBase nbtBase) {
        if (nbtBase instanceof NBTTagCompound) {
            provided.deserializeNBT((NBTTagCompound) nbtBase);
        }
    }
}
