package de.webtwob.mma.core.common.inventory;

import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.api.util.MMAFilter;
import de.webtwob.mma.core.common.tileentity.TileEntityQueue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 13. Dez. 2017.
 */
public class QueueContainer extends Container implements IInventoryChangedListener {

    int xSize = 176;
    int ySize = 220;
    private TileEntityQueue entityQueue;
    private EntityPlayer    player;
    private InventoryBasic request = new InventoryBasic("QueueEnqueueSlot", false, 1) {
        @Override
        public int getInventoryStackLimit() {
            return 1;
        }
    };

    public QueueContainer(TileEntityQueue queue, EntityPlayer player) {
        entityQueue = queue;
        this.player = player;
        positionPlayerSlots();
        request.addInventoryChangeListener(this);
        addSlotToContainer(new Slot(request, 0, 8, 7) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return MMAFilter.checkIfNotNull(ICraftingRequest.getCraftingRequest(stack), MMAFilter.REQUEST_NOT_DONE);
            }
        });
    }

    public static QueueContainer tryCreateInstance(EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityQueue) {
            return new QueueContainer((TileEntityQueue) te, player);
        }
        return null;
    }

    private void positionPlayerSlots() {
        for (int row = 0; 3 > row; row++) {
            for (int col = 0; 9 > col; col++) {
                addSlotToContainer(
                        new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, ySize - 10 - (4 - row) * 18));
            }
        }

        for (int hotbar = 0; 9 > hotbar; hotbar++) {
            addSlotToContainer(new Slot(player.inventory, hotbar, 8 + hotbar * 18, ySize - 24));
        }
    }

    /**
     * This function is called when a Player shift-clicks a Slot in a Inventory
     * returns the remaining ItemStack in the Shift-Clicked slot if the ItemStack has been partially transferred
     * else it returns ItemStack.EMPTY
     *
     * @param playerIn the player that Shift-Clicked
     * @param index    the index of the Slot that got Shift-Clicked
     */
    @Nonnull
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        Slot slot = getSlot(index);

        //Slot non-existent or Empty
        //noinspection ConstantConditions
        if (slot == null || !slot.getHasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack          = slot.getStack();
        int       originalAmount = stack.getCount();
        if (36 <= index) {//this container displays 36 of the Players inventory slots(no Off-Hand)
            //From Block to Player Inventory
            if (!mergeItemStack(stack, 0, 36, false)) {
                //could not merge into player inventory
                return ItemStack.EMPTY;
            }
        } else {
            //From Player to Block Inventory
            if (getSlot(36).isItemValid(stack) && mergeItemStack(stack, 36, 37, false)) {
                if (stack.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                }
                slot.onSlotChanged();
                slot.onTake(player, stack);
            }
        }
        if (stack.getCount() != originalAmount) {
            //slot contant changed
            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            slot.onSlotChanged();
            slot.onTake(player, stack);
        }

        return ItemStack.EMPTY;

    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onInventoryChanged(@Nonnull final IInventory invBasic) {
        ItemStack stack = invBasic.getStackInSlot(0);
        if (!stack.isEmpty() && entityQueue.canStackBeAddedToQueue(stack)) {
            stack = invBasic.removeStackFromSlot(0);
            entityQueue.addStackToQueue(stack);
            entityQueue.invalidate();
            entityQueue.markDirty();
        }
    }

    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        clearContainer(playerIn, playerIn.world, request);
    }
}
