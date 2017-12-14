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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 13. Dez. 2017.
 */
public class QueueContainer extends Container implements IInventoryChangedListener {
    
    private static Capability<ICraftingRequest> capabilityCraftingRequest;
    
    @CapabilityInject(ICraftingRequest.class)
    private static void injectCraftingRequest(Capability<ICraftingRequest> handler) {
        capabilityCraftingRequest = handler;
    }
    
    private TileEntityQueue entityQueue;
    private EntityPlayer    player;
    private InventoryBasic request = new InventoryBasic("QueueEnqueueSlot", false, 1) {
        @Override
        public int getInventoryStackLimit() {
            return 1;
        }
    };
    
    int xSize = 176;
    int ySize = 220;
    
    public QueueContainer(TileEntityQueue queue,EntityPlayer player) {
        entityQueue = queue;
        this.player = player;
        positionPlayerSlots();
        request.addInventoryChangeListener(this);
        addSlotToContainer(new Slot(request, 0, 8, 7) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return null!= capabilityCraftingRequest && MMAFilter.checkIfNotNull(stack.getCapability(capabilityCraftingRequest,null),MMAFilter.REQUEST_NOT_DONE);
            }
        });
    }
    
    public static QueueContainer tryCreateInstance(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityQueue) {
            return new QueueContainer((TileEntityQueue) te,player);
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
        }
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        clearContainer(playerIn,playerIn.world,request);
    }
}
