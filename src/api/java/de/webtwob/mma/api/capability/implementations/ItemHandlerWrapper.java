package de.webtwob.mma.api.capability.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by BB20101997 on 07. Mai. 2017.
 * <p>
 * Fixes the ItemStackHandler's saving and loading functionality so we don't have to use a NonNullList
 * this is needed for the FilteredItemHandler, CombinedItemHandler
 * since they all may wrap another ItemStackHandler
 */
public abstract class ItemHandlerWrapper extends ItemStackHandler {

    /**
     * An ItemHandler that doesn't rely on the {@literal NonNullList<ItemStack>} directly for saving to/loading from NBT
     */
    public ItemHandlerWrapper() {
        super();
    }

    /**
     * An ItemHandler that doesn't rely on the {@literal NonNullList<ItemStack>} directly for saving to/loading from NBT
     *
     * @param stacks a {@literal NonNullList<ItemStacks>} to store the ItemStacks for this ItemStackHandler in
     */
    public ItemHandlerWrapper(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        ItemStack  stack;
        for (int i = 0; i < getSlots(); i++) {
            stack = getStackInSlot(i);
            if (!stack.isEmpty()) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stack.writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", getSlots());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int            slot     = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < getSlots()) {
                setStackInSlot(slot, new ItemStack(itemTags));
            }
        }
        onLoad();
    }

}
