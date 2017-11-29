package de.webtwob.mma.api.capability.provider;

import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.api.references.NBTKeys;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mma.api.capability.APICapabilities.CAPABILITY_CRAFTING_REQUEST;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class CraftingRequestProvider implements ICapabilitySerializable {

    private ItemStack item;
    private ICraftingRequest provider = new ICraftingRequest() {

        @Override
        public boolean isCompleted() {
            NBTTagCompound compound = item.getTagCompound();
            return compound == null || compound.getInteger(NBTKeys.TOKEN_SHARE_QUANTITY) <= 0 || new ItemStack(
                    compound.getCompoundTag(NBTKeys.TOKEN_SHARE_REQUEST)).isEmpty();
        }

        @Override
        public void reduceQuantity(final int i) {
            NBTTagCompound compound = item.getTagCompound();
            if (compound == null) {
                compound = new NBTTagCompound();
            }
            compound.setInteger(NBTKeys.TOKEN_SHARE_QUANTITY,
                    Math.max(0, compound.getInteger(NBTKeys.TOKEN_SHARE_QUANTITY) - i));
            item.setTagCompound(compound);
        }

        @Override
        public int getQuantity() {
            NBTTagCompound compound = item.getTagCompound();
            if (compound == null) {
                return 0;
            }
            return compound.getInteger(NBTKeys.TOKEN_SHARE_QUANTITY);
        }

        @Override
        public void setQuantity(final int amount) {
            NBTTagCompound compound = item.getTagCompound();
            if (compound == null) {
                compound = new NBTTagCompound();
            }
            compound.setInteger(NBTKeys.TOKEN_SHARE_QUANTITY, amount);
            item.setTagCompound(compound);
        }

        @Nonnull
        @Override
        public ItemStack getRequest() {
            NBTTagCompound compound = item.getTagCompound();
            if (compound == null) {
                return ItemStack.EMPTY;
            }
            return new ItemStack(compound.getCompoundTag(NBTKeys.TOKEN_SHARE_REQUEST));
        }

        @Override
        public void setRequest(@Nonnull ItemStack stack) {
            NBTTagCompound compound = item.getTagCompound();
            if (compound == null) {
                compound = new NBTTagCompound();
            }
            compound.setTag(NBTKeys.TOKEN_SHARE_REQUEST, stack.serializeNBT());
            item.setTagCompound(compound);
        }
    };

    /**
     * @param stack the ItemStack for which to provide an instance of ICraftingRequest
     * */
    public CraftingRequestProvider(ItemStack stack) {
        item = stack;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY_CRAFTING_REQUEST;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? (T) provider : null;
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
