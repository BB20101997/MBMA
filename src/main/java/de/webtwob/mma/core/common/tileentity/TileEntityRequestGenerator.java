package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.capability.APICapabilities;
import de.webtwob.mma.api.capability.implementations.CombinedItemHandler;
import de.webtwob.mma.api.capability.implementations.FilteredItemHandler;
import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.api.interfaces.gui.IGUIHandlerBoth;
import de.webtwob.mma.api.util.MMAFilter;
import de.webtwob.mma.core.client.gui.TokenGeneratorGui;
import de.webtwob.mma.core.common.inventory.TokenGeneratorContainer;
import de.webtwob.mma.core.common.references.NBTKeys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by bennet on 28.03.17.
 */
public class TileEntityRequestGenerator extends TileEntity implements ITickable, ISidedInventory, IGUIHandlerBoth {

    private static final String NBT_KEY_CNAME = "CustomName";

    private final NonNullList<ItemStack> musterList = NonNullList.withSize(5, ItemStack.EMPTY);

    private final NonNullList<ItemStack> outputList = NonNullList.withSize(5, ItemStack.EMPTY);

    //we need different list so we can have different ItemHandlers later for the correct sidedness
    private final NonNullList<ItemStack> upList    = NonNullList.withSize(1, ItemStack.EMPTY);
    private final NonNullList<ItemStack> northList = NonNullList.withSize(1, ItemStack.EMPTY);
    private final NonNullList<ItemStack> eastList  = NonNullList.withSize(1, ItemStack.EMPTY);
    private final NonNullList<ItemStack> southList = NonNullList.withSize(1, ItemStack.EMPTY);
    private final NonNullList<ItemStack> westList  = NonNullList.withSize(1, ItemStack.EMPTY);

    @SuppressWarnings("unchecked")
    private final NonNullList<ItemStack>[] dirLists = (NonNullList<ItemStack>[]) (new NonNullList[]{
            northList, eastList, southList, westList, upList
    });//NOSONAR

    private final ItemStackHandler muster = new FilteredItemHandler(musterList, MMAFilter.MUSTER_FILTER, 1);

    private final ItemStackHandler output = new FilteredItemHandler(outputList, o -> false, 64);

    private final ItemStackHandler up    = new FilteredItemHandler(upList, MMAFilter.INPUT_FILTER, 64);
    private final ItemStackHandler north = new FilteredItemHandler(northList, MMAFilter.INPUT_FILTER, 64);
    private final ItemStackHandler east  = new FilteredItemHandler(eastList, MMAFilter.INPUT_FILTER, 64);
    private final ItemStackHandler south = new FilteredItemHandler(southList, MMAFilter.INPUT_FILTER, 64);
    private final ItemStackHandler west  = new FilteredItemHandler(westList, MMAFilter.INPUT_FILTER, 64);

    private final ItemStackHandler combined = new CombinedItemHandler(north, east, south, west, up, output);

    private String customName;
    private int idleTimer = 0;

    public ItemStackHandler getMuster() {
        return muster;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        NBTTagCompound compound = getTileData();
        if (compound.hasKey(NBTKeys.TOKEN_GENERATOR_MUSTER)) {
            muster.deserializeNBT((NBTTagCompound) compound.getTag(NBTKeys.TOKEN_GENERATOR_MUSTER));
        }
        if (compound.hasKey(NBTKeys.TOKEN_GENERATOR_COMBINED)) {
            combined.deserializeNBT((NBTTagCompound) compound.getTag(NBTKeys.TOKEN_GENERATOR_COMBINED));
        }
        if (nbtTagCompound.hasKey(NBT_KEY_CNAME, 8)) {
            this.setCustomName(nbtTagCompound.getString(NBT_KEY_CNAME));
        }

    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        NBTTagCompound compound = getTileData();
        compound.setTag(NBTKeys.TOKEN_GENERATOR_MUSTER, muster.serializeNBT());
        compound.setTag(NBTKeys.TOKEN_GENERATOR_COMBINED, combined.serializeNBT());

        nbtTagCompound = super.writeToNBT(nbtTagCompound);

        if (this.hasCustomName()) {
            nbtTagCompound.setString(NBT_KEY_CNAME, getName());
        }

        return nbtTagCompound;
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
                default:
                    throw new IllegalArgumentException(String.format(
                            "Parameter of type EnumFacing is neither null nor one of the six possible values: %s %nWho added a dimension to the universe without telling me?",
                            facing.toString()
                    ));
            }
        }
    }

    @Override
    public void update() {
        if (idleTimer <= 0) {
            for (int slot = 0; slot < 5; slot++) {
                if (canProccesForSlot(slot)) {
                    ItemStack extract = combined.extractItem(slot, 1, false);
                    if (!outputList.get(slot).isEmpty()) {
                        outputList.get(slot).grow(1);
                        continue;
                    }
                    ICraftingRequest request = extract.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null);
                    ICraftingRequest musterRequest = muster.getStackInSlot(slot)
                                                           .getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST,
                                                                          null
                                                           );
                    copyRequest(musterRequest, request);
                    outputList.set(slot, extract);
                }
            }
            markDirty();
            idleTimer = 20;
        } else {
            idleTimer--;
        }
    }

    private void copyRequest(ICraftingRequest musterRequest, ICraftingRequest request) {
        if (musterRequest != null && request != null) {
            request.setRequest(musterRequest.getRequest());
            request.setQuantity(musterRequest.getQuantity());
        }
    }

    private boolean canProccesForSlot(int slot) {
        if (slot < 5 && slot >= 0) {
            ItemStack extract  = combined.extractItem(slot, 1, true);
            ItemStack out      = output.getStackInSlot(slot);
            ItemStack template = muster.getStackInSlot(slot);

            if (template.isEmpty() || extract.isEmpty()) {
                return false;
            }
            if (out.isEmpty()) {
                return true;
            }
            if (out.getCount() >= output.getSlotLimit(slot)) {
                return false;
            }
            if (out.getCount() >= out.getItem().getItemStackLimit(out)) {
                return false;
            }

            copyRequest(template.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null),
                        extract.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)
            );

            return ItemHandlerHelper.canItemStacksStack(out, extract);
        }
        return false;
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
            default:
                return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        }
    }

    @Override
    public boolean canInsertItem(int slot, @Nonnull ItemStack itemStack, @Nonnull EnumFacing enumFacing) {
        boolean flag = enumFacing != EnumFacing.DOWN && slot <= 4 && MMAFilter.INPUT_FILTER.test(itemStack);
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
        if (slot < 0) {
            throw new IllegalArgumentException("Slot Index may not be Negative!");
        } else if (slot > 4) {
            return outputList.get(slot - 5);
        } else {
            return dirLists[slot].get(0);
        }
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack retrieved = combined.extractItem(slot, amount, false);
        if (!retrieved.isEmpty()) {
            markDirty();
        }
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
        if (slot < 0) {
            throw new IllegalArgumentException("Slot Index may not be Negative!");
        } else if (slot > 4) {
            outputList.set(slot - 5, itemStack);
        } else {
            dirLists[slot].set(0, itemStack);
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
        //no action required for InventoryOpen
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer entityPlayer) {
        //no action required for InventoryOpen
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack itemStack) {
        return slot <= 4 && MMAFilter.INPUT_FILTER.test(itemStack);
    }

    @Override
    public int getField(int i) {
        return 0;
    }

    @Override
    public void setField(int i, int i1) {
        //NO-OP
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
        return hasCustomName() ? customName : "mma.inventory.token.generator";
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && !customName.isEmpty();
    }

    private void setCustomName(String customName) {
        this.customName = customName;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return TokenGeneratorGui.tryCreateInstance(player, world.getTileEntity(new BlockPos(x, y, z)));
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return TokenGeneratorContainer.tryCreateInstance(player, world.getTileEntity(new BlockPos(x, y, z)));
    }
}
