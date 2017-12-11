package de.webtwob.mma.core.common.inventory;

import de.webtwob.mma.core.common.tileentity.TileEntityRequestGenerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by bennet on 28.03.17.
 */
public class TokenGeneratorContainer extends Container {

    int xSize = 230;
    int ySize = 168;
    int inputY = 7;
    int outputY = 68;
    int dif = 54;
    int inputOffset = 17;
    int outputOffset = 26;
    int lastDif = 81;
    private EntityPlayer player;
    private IItemHandler muster;
    private IItemHandler combined;

    public TokenGeneratorContainer(EntityPlayer player0, TileEntityRequestGenerator tgte) {
        player = player0;
        muster = tgte.getMuster();
        combined = tgte.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        positionPlayerSlots();
        positionTokenGeneratorSlots();
    }

    private void positionTokenGeneratorSlots() {
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotItemHandler(combined, i, i * dif + inputOffset, inputY));
            addSlotToContainer(new SlotItemHandler(muster, i, i * dif + inputOffset + 18, inputY));
            addSlotToContainer(new SlotItemHandler(combined, i + 5, i * dif + outputOffset, outputY));
        }

        addSlotToContainer(new SlotItemHandler(combined, 4, 3 * dif + inputOffset, inputY + lastDif));
        addSlotToContainer(new SlotItemHandler(muster, 4, 3 * dif + inputOffset + 18, inputY + lastDif));
        addSlotToContainer(new SlotItemHandler(combined, 9, 3 * dif + outputOffset, outputY + lastDif));

    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {

        ItemStack ret = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {

            ItemStack inSlot = slot.getStack();
            ret = inSlot.copy();

            //from player inventory
            if (index < 4 * 9) {
                if (!mergeItemStack(inSlot, 4 * 9, inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(inSlot, 0, 4 * 9, true)) {
                return ItemStack.EMPTY;
            }
            if (inSlot.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return ret;

    }

    private void positionPlayerSlots() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, ySize - 7 - (4 - row) * 18));
            }
        }

        for (int hotbar = 0; hotbar < 9; hotbar++) {
            addSlotToContainer(new Slot(player.inventory, hotbar, 8 + hotbar * 18, ySize - 21));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer entityPlayer) {
        return true;
    }
    
    public static TokenGeneratorContainer tryCreateInstance(final EntityPlayer player, final TileEntity tileEntity) {
        if(tileEntity instanceof TileEntityRequestGenerator){
            return new TokenGeneratorContainer(player, (TileEntityRequestGenerator) tileEntity);
        }
        return null;
    }
}
