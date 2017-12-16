package de.webtwob.mma.core.common.inventory;

import de.webtwob.mma.api.util.MMAFilter;
import de.webtwob.mma.core.common.tileentity.TileEntityCraftingController;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 03. Dez. 2017.
 */
public class CraftingControllerContainer extends Container implements IInventoryChangedListener {

    int xSize = 176;
    int ySize = 220;
    private InventoryBasic linkcard = new InventoryBasic("CraftingControllerLinkCardInput", false, 1) {
        @Override
        public int getInventoryStackLimit() {
            return 1;
        }
    };
    private EntityPlayer                 player;
    private TileEntityCraftingController tileEntityCraftingController;

    public CraftingControllerContainer(EntityPlayer player, TileEntityCraftingController tecc) {
        this.player = player;
        tileEntityCraftingController = tecc;
        positionPlayerSlots();
        linkcard.addInventoryChangeListener(this);
        addSlotToContainer(new Slot(this.linkcard, 0, 8, 7) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return MMAFilter.LINK_FILTER.test(stack);
            }
        });

    }

    public static CraftingControllerContainer tryCreateInstance(final EntityPlayer player, final TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityCraftingController) {
            return new CraftingControllerContainer(player, (TileEntityCraftingController) tileEntity);
        }
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
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

    @Override
    public void onInventoryChanged(@Nonnull IInventory invBasic) {
        ItemStack stack = invBasic.getStackInSlot(0);
        if (!stack.isEmpty() && tileEntityCraftingController.canAddLinkCard()) {
            stack = invBasic.removeStackFromSlot(0);
            tileEntityCraftingController.addLinkCard(stack);
        }
    }

    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        clearContainer(playerIn, playerIn.world, linkcard);
    }
}
