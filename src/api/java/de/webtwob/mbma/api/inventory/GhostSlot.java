package de.webtwob.mbma.api.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by bennet on 22.03.17.
 */
public class GhostSlot extends Slot {

    //TODO redo this
    
    public GhostSlot(@Nonnull IInventory inventory,int slot, int xPosition, int yPosition) {
        //noinspection ConstantConditions
        super(inventory, slot, xPosition, yPosition);
    }

    public void setItemStack(ItemStack stack) {
       this.inventory.setInventorySlotContents(slotNumber,stack);
    }

    @Override
    public int getSlotStackLimit() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return true;
    }
}
