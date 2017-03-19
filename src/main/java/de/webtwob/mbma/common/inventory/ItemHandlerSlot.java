package de.webtwob.mbma.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
public class ItemHandlerSlot extends Slot {

    public ItemHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(new ItemHandlerInventor(itemHandler, index), 0, xPosition, yPosition);
    }

    private static class ItemHandlerInventor implements IInventory {

        final IItemHandler itemHandler;
        final int          slot;

        @Override
        public int getSizeInventory() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return itemHandler.getStackInSlot(slot).isEmpty();
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            return itemHandler.getStackInSlot(slot);
        }

        @Override
        public ItemStack decrStackSize(int index, int count) {
            return itemHandler.extractItem(slot, count, false);
        }

        @Override
        public ItemStack removeStackFromSlot(int index) {
            return itemHandler.extractItem(slot, getStackInSlot(slot).getCount(), false);
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
            itemHandler.insertItem(slot, stack, false);
        }

        @Override
        public int getInventoryStackLimit() {
            return itemHandler.getSlotLimit(slot);
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean isUsableByPlayer(EntityPlayer player) {
            return true;
        }

        @Override
        public void openInventory(EntityPlayer player) {

        }

        @Override
        public void closeInventory(EntityPlayer player) {

        }

        @Override
        public boolean isItemValidForSlot(int index, ItemStack stack) {
            return itemHandler.getStackInSlot(slot).getItem() == null || itemHandler.getStackInSlot(slot).getItem()
                                                                                 == stack.getItem();
        }

        @Override
        public int getField(int id) {
            return 0;
        }

        @Override
        public void setField(int id, int value) {

        }

        @Override
        public int getFieldCount() {
            return 0;
        }

        @Override
        public void clear() {

        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        public ITextComponent getDisplayName() {
            return null;
        }

        public ItemHandlerInventor(IItemHandler itemHandler, int slot) {
            this.itemHandler = itemHandler;
            this.slot = slot;
        }
    }
}
