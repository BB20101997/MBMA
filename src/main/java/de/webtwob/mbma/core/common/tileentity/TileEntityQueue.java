package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.registries.MultiBlockGroupType;
import de.webtwob.mbma.core.common.config.MBMAConfiguration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.LinkedList;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityQueue extends MultiBlockTileEntity implements ISidedInventory {
    
    @SuppressWarnings("WeakerAccess")
    @ObjectHolder("mbmacore:queue")
    public static final MultiBlockGroupType MANAGER_QUEUE = null;
    private final ItemStack[] requests = new ItemStack[MBMAConfiguration.queueLenght];
    private final LinkedList<ItemStack> requests;
    
    {
        requests = new LinkedList<>();
    }
    
    public MultiBlockGroupType getGroupType() {
        return MANAGER_QUEUE;
    }
    
    @Override
    public int[] getSlotsForFace(final EnumFacing side) {
        return new int[]{0,1};
    }
    
    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
        
        return false;
    }
    
    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
        return false;
    }
    
    @Override
    public int getSizeInventory() {
        return 0;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return null;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        return null;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
    
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 0;
    }
    
    @Override
    public boolean isUsableByPlayer(final EntityPlayer player) {
        return false;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
    
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return false;
    }
    
    @Override
    public int getField(final int id) {
        return 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
    
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
}
