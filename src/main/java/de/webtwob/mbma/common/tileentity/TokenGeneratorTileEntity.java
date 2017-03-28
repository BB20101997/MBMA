package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.common.capability.CombinedItemHandler;
import de.webtwob.mbma.common.capability.FilteredItemHandler;
import de.webtwob.mbma.common.interfaces.IStackFilter;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by bennet on 28.03.17.
 */
public class TokenGeneratorTileEntity extends TileEntity implements ITickable, ISidedInventory {

    private static final IStackFilter OUTPUT_FILTER = (e) -> false;
    private static final IStackFilter MUSTER_FILTER = (e) -> {
        ICraftingRequest request;
        if (e != null && (request = e.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
            if (!request.getRequest().isEmpty()) {
                return true;
            }
        }
        return false;
    };
    private static final IStackFilter INPUT_FILTER = (e) -> {
        ICraftingRequest request;
        if (e != null && (request = e.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
            if (request.getRequest().isEmpty() || request.isCompleted()) {
                return true;
            }
        }
        return false;
    };

    private final NonNullList<ItemStack> outputList = NonNullList.withSize(5, ItemStack.EMPTY);
    private final NonNullList<ItemStack> musterList = NonNullList.withSize(5, ItemStack.EMPTY);
    private final NonNullList<ItemStack> upList = NonNullList.withSize(1, ItemStack.EMPTY);
    private final NonNullList<ItemStack> northList = NonNullList.withSize(1, ItemStack.EMPTY);
    private final NonNullList<ItemStack> eastList = NonNullList.withSize(1, ItemStack.EMPTY);
    private final NonNullList<ItemStack> southList = NonNullList.withSize(1, ItemStack.EMPTY);
    private final NonNullList<ItemStack> westList = NonNullList.withSize(1, ItemStack.EMPTY);

    private final ItemStackHandler muster = new FilteredItemHandler(musterList, MUSTER_FILTER, 1);

    private final ItemStackHandler output = new FilteredItemHandler(outputList, OUTPUT_FILTER, 1);
    private final ItemStackHandler up = new FilteredItemHandler(upList, INPUT_FILTER, 1);
    private final ItemStackHandler north = new FilteredItemHandler(northList, INPUT_FILTER, 1);
    private final ItemStackHandler east = new FilteredItemHandler(eastList, INPUT_FILTER, 1);
    private final ItemStackHandler south = new FilteredItemHandler(southList, INPUT_FILTER, 1);
    private final ItemStackHandler west = new FilteredItemHandler(westList, INPUT_FILTER, 1);

    private final ItemStackHandler combined = new CombinedItemHandler(north, east, south, west, up, output);
    private String customName;

    public ItemStackHandler getMuster() {
        return muster;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        NBTTagCompound compound = getTileData();
        compound.setTag(MBMA_NBTKeys.TOKEN_GENERATOR_MUSTER, muster.serializeNBT());
        compound.setTag(MBMA_NBTKeys.TOKEN_GENERATOR_COMBINED, combined.serializeNBT());

        nbtTagCompound = super.writeToNBT(nbtTagCompound);

        if (this.hasCustomName()) {
            nbtTagCompound.setString("CustomName", getName());
        }

        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        NBTTagCompound compound = getTileData();
        if (compound.hasKey(MBMA_NBTKeys.TOKEN_GENERATOR_MUSTER)) {
            muster.deserializeNBT((NBTTagCompound) compound.getTag(MBMA_NBTKeys.TOKEN_GENERATOR_MUSTER));
        }
        if (compound.hasKey(MBMA_NBTKeys.TOKEN_GENERATOR_COMBINED)) {
            combined.deserializeNBT((NBTTagCompound) compound.getTag(MBMA_NBTKeys.TOKEN_GENERATOR_COMBINED));
        }
        if (nbtTagCompound.hasKey("CustomName", 8)) {
            this.setCustomName(nbtTagCompound.getString("CustomName"));
        }


    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) getItemHandlerFromFacing(facing);
        }
        return super.getCapability(capability, facing);
    }

    private IItemHandler getItemHandlerFromFacing(EnumFacing facing) {
        if (facing == null) {
            return combined;
        } else {
            switch (facing) {
                case DOWN:
                    return output;
                case UP:
                    return up;
                case NORTH:
                    return north;
                case SOUTH:
                    return south;
                case WEST:
                    return west;
                case EAST:
                    return east;
                default: {
                    throw new RuntimeException(String.format("Parameter of type EnumFacing is neither null nor one of the six possible values: %s \nWho added a dimension to the universe without telling me?", facing.toString()));
                }
            }
        }
    }

    private int idleTimer = 0;

    @Override
    public void update() {
        if (idleTimer <= 0) {
            for (int slot = 0; slot < 5; slot++) {
                if (output.getStackInSlot(slot).isEmpty() && !muster.getStackInSlot(slot).isEmpty() && !combined.getStackInSlot(slot).isEmpty()) {
                    ItemStack insert, extract = combined.extractItem(slot, 1, false);
                    if (!extract.isEmpty()) {
                        insert = ItemHandlerHelper.copyStackWithSize(muster.getStackInSlot(slot), 1);
                        outputList.set(slot, insert);
                        markDirty();
                    }
                }
            }
            idleTimer = 20;
        } else {
            idleTimer--;
        }
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing enumFacing) {
        switch (enumFacing) {
            case DOWN:
                return new int[]{5, 6, 7, 8, 9};
            case UP:
                return new int[]{4};
            case NORTH:
                return new int[]{0};
            case EAST:
                return new int[]{1};
            case SOUTH:
                return new int[]{2};
            case WEST:
                return new int[]{3};
        }
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    @Override
    public boolean canInsertItem(int slot, @Nonnull ItemStack itemStack, @Nonnull EnumFacing enumFacing) {
        boolean flag = enumFacing != EnumFacing.DOWN && slot <= 4 && INPUT_FILTER.accept(itemStack);
        return flag && combined.getStackInSlot(slot).isEmpty();
    }

    @Override
    public boolean canExtractItem(int i, @Nonnull ItemStack itemStack, @Nonnull EnumFacing enumFacing) {
        return enumFacing == EnumFacing.DOWN && i > 4;
    }

    @Override
    public int getSizeInventory() {
        return combined.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int slot = 0; slot < combined.getSlots(); slot++) {
            if (!combined.getStackInSlot(slot).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot > 4) {
            return outputList.get(slot - 5);
        }
        switch (slot) {
            case 0: {
                return northList.get(0);
            }
            case 1: {
                return eastList.get(0);
            }
            case 2: {
                return southList.get(0);
            }
            case 3: {
                return westList.get(0);
            }
            case 4: {
                return upList.get(0);
            }
            default: {
                return ItemStack.EMPTY;
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack retrieved = combined.extractItem(slot, amount, false);
        if(!retrieved.isEmpty())
            markDirty();
        return retrieved;
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack retrieved = combined.extractItem(slot, combined.getStackInSlot(slot).getCount(), false);
        if (!retrieved.isEmpty()) {
            markDirty();
        }
        return retrieved;
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack itemStack) {
        if (slot <= 4) {
            switch (slot){
                case 0:{
                    northList.set(0,itemStack);
                    break;
                }
                case 1:{
                    eastList.set(0,itemStack);
                    break;
                }
                case 2:{
                    southList.set(0,itemStack);
                    break;
                }
                case 3:{
                    westList.set(0,itemStack);
                    break;
                }
                case 4:{
                    upList.set(0,itemStack);
                    break;
                }
            }
        } else {
            outputList.set(slot, itemStack);
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer entityPlayer) {
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer entityPlayer) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack itemStack) {
        if (slot > 4) {
            return OUTPUT_FILTER.accept(itemStack);
        } else {
            return INPUT_FILTER.accept(itemStack);
        }
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int i1) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int slot = 0; slot < combined.getSlots(); slot++) {
            combined.setStackInSlot(slot, ItemStack.EMPTY);
        }
        markDirty();
    }

    @Nonnull
    @Override
    public String getName() {
        return hasCustomName() ? customName : "mbma.inventory.token.generator";
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && !customName.isEmpty();
    }

    private void setCustomName(String customName) {
        this.customName = customName;
    }
}
