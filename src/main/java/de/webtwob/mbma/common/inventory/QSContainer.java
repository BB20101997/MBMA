package de.webtwob.mbma.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
public class QSContainer extends Container {

    int xSize = 176;
    int ySize = 220;

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {

        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

        ItemStack ret = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if(slot!=null&&slot.getHasStack()){

            ItemStack inSlot = slot.getStack();
            ret = inSlot.copy();

            //from player inventory
            if(index < 4*9 ){
                if(!mergeItemStack(inSlot,4*9,this.inventorySlots.size(),false)){
                    return ItemStack.EMPTY;
                }
            }else if(!mergeItemStack(inSlot,0,4*9,true)){
                return  ItemStack.EMPTY;
            }
            if(inSlot.isEmpty()){
                slot.putStack(ItemStack.EMPTY);
            }else{
                slot.onSlotChanged();
            }

        }
        return ret;
    }

    public QSContainer(InventoryPlayer playerInv, IItemHandler qsInventory, EntityPlayer player) {
        layoutPlayerInventory(playerInv);
        layoutQSInventory(qsInventory);
    }

    private void layoutQSInventory(IItemHandler qsInventory) {
        for(int col = 0; col < 3; col++) {
            for(int row = 0; row < 6; ++row) {
                addSlotToContainer(new SlotItemHandler(qsInventory, row + col * 6, 8 + col * 18, 18 + 18 * row));
            }
        }
    }

    protected void layoutPlayerInventory(InventoryPlayer player) {
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(player, col + row * 9 + 9, 8 + col * 18, ySize - 10 - (4 - row) * 18));
            }
        }

        for(int hotbar = 0; hotbar < 9; hotbar++) {
            addSlotToContainer(new Slot(player, hotbar, 8 + hotbar * 18, ySize - 24));
        }
    }
}
