package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.api.MBMAProperties;
import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.enums.MaschineState;
import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.MBMAPacketHandler;
import de.webtwob.mbma.common.capability.QSItemHandler;
import de.webtwob.mbma.common.interfaces.IMaschineState;
import de.webtwob.mbma.common.packet.MaschineStateUpdatePacket;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

import static de.webtwob.mbma.common.references.MBMA_NBTKeys.QS_STATE;
import static de.webtwob.mbma.common.references.MBMA_NBTKeys.QS_TOKEN;

/**
 * Created by bennet on 17.03.17.
 */
public class QSTileEntity extends TileEntity implements ITickable, IMaschineState {

    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> ITEM_HANDLER = null;

    private final QSItemHandler itemHandler = new QSItemHandler(this);
    @Nonnull
    private List<String> errorMessage;
    private ItemStack token = ItemStack.EMPTY;
    private int idleTimer = 0;
    private MaschineState maschineState = MaschineState.PROBLEM;

    public List<String> getErrorMessages() {
        return errorMessage;
    }

    /**
     * Moves a specified amount of an ItemStack from Permanent Storage to Tempory Storage
     *
     * @param linkId   the linkcard that points to the permanent inventory
     * @param permSlot the slot in the permanent inventory
     * @param amount   the amount to move
     */
    private boolean moveStackToTemp(int linkId, int permSlot, int amount, boolean simulate) {
        if (!simulate && !moveStackToTemp(linkId, permSlot, amount, true)) {
            return false;
        }
        ItemStack link = itemHandler.getStackInSlot(linkId);
        IItemHandler permInventory = getInventoryFromLink(link);
        boolean success = true;
        if (permInventory != null) {
            ItemStack itemStack = permInventory.extractItem(permSlot, amount, simulate);
            if (itemStack.getCount() != amount) {
                success = false;
            }
            IItemHandler tempInventory;
            for (int i = 0; i < 6 && !itemStack.isEmpty(); i++) {
                tempInventory = getInventoryFromLink(itemHandler.getStackInSlot(i + 6));
                if (tempInventory == null) {
                    continue;
                }
                for (int slot = 0; slot < tempInventory.getSlots() && !itemStack.isEmpty(); slot++) {
                    itemStack = tempInventory.insertItem(slot, itemStack, simulate);
                }
            }
            if (itemStack.isEmpty()) {
                return success;
            }else{
                itemStack = permInventory.insertItem(permSlot,itemStack,simulate);
                if(!itemStack.isEmpty()){
                    world.spawnEntity(new EntityItem(world,getPos().getX(),getPos().getY(),getPos().getZ(),itemStack));
                }
                return false;
            }
        }

        return false;
    }

    @Nullable
    private IItemHandler getInventoryFromLink(ItemStack itemStack) {
        BlockPos pos = getLinkFromItemStack(itemStack);
        World world = getWorld();
        if (pos != null) {
            IBlockState state = world.getBlockState(pos);
            if (state.getValue(MBMAProperties.CONNECTED)) {
                EnumFacing dir = state.getValue(MBMAProperties.FACING);
                TileEntity te = world.getTileEntity(pos.offset(dir));
                if (te != null) {
                    return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite());
                }
            }
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound td = getTileData();
        token = new ItemStack(td.getCompoundTag(QS_TOKEN));
        ITEM_HANDLER.getStorage().readNBT(ITEM_HANDLER, itemHandler, null, td.getTag(MBMA_NBTKeys.QS_ITEM_HANDLER));
        setMaschineState(MaschineState.values()[td.getInteger(QS_STATE)]);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound td = getTileData();
        NBTBase itemHandler = ITEM_HANDLER.getStorage().writeNBT(ITEM_HANDLER, this.itemHandler, null);
        if (itemHandler != null) {
            td.setTag(MBMA_NBTKeys.QS_ITEM_HANDLER, itemHandler);
        }
        td.setTag(QS_TOKEN, token.serializeNBT());
        td.setInteger(QS_STATE, maschineState.ordinal());
        compound = super.writeToNBT(compound);
        return compound;
    }


    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return (ITEM_HANDLER != null && capability == ITEM_HANDLER) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (ITEM_HANDLER != null && capability == ITEM_HANDLER) {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if (!world.isRemote)
            if (idleTimer < 0) {
                switch (getMaschineState()) {
                    case IDLE:
                        runIdleTasks();
                        break;
                    case RUNNING:
                        runRunningTask();
                        break;
                    case WAITING:
                        runWaitingTask();
                        break;
                    case PROBLEM:
                        runProblemTask();
                        break;
                }
            } else {
                idleTimer--;
            }
    }

    private void runIdleTasks() {
        findNewToken();
        if (!token.isEmpty()) {
            setMaschineState(MaschineState.RUNNING);
        }
        idleTimer = 20;
    }

    private void runRunningTask() {
        if (token.isEmpty()) {
            setMaschineState(MaschineState.IDLE);
            return;
        }

        //work on the current token
    }

    private void runWaitingTask() {

    }

    private void runProblemTask() {
        boolean noPermanentStorage = true;
        boolean noTemporaryStorage = true;
        //TODO set to true once implemented
        boolean noRecipeBank = false;
        for (int i = 0; (noTemporaryStorage || noRecipeBank || noPermanentStorage) && i < 6; i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                noPermanentStorage = false;
            }
            if (!itemHandler.getStackInSlot(i + 6).isEmpty()) {
                noTemporaryStorage = false;
            }
            if (!itemHandler.getStackInSlot(i + 12).isEmpty()) {
                noRecipeBank = false;
            }
        }
        if (!(noPermanentStorage || noTemporaryStorage || noRecipeBank)) {
            setMaschineState(MaschineState.IDLE);
        } else {
            if (noPermanentStorage) errorMessage.add("text.mbma:qs.error.permmissing");
            if (noTemporaryStorage) errorMessage.add("text.mbma:qs.error.tempmissing");
            if (noRecipeBank) errorMessage.add("text.mbma:qs.error.recipebank");
        }
        idleTimer = 20;
    }

    @Nonnull
    public MaschineState getMaschineState() {
        return maschineState;
    }

    private static BlockPos getLinkFromItemStack(ItemStack itemStack) {
        IBlockPosProvider bpp = itemStack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null);
        return bpp != null ? bpp.getBlockPos() : null;
    }

    private void findNewToken() {
        if (!world.isRemote) {
            boolean noLink = true;
            //iterate over the Linkcard for Permanent Storage Interfaces
            for (int i = 0; i < 6; i++) {
                IItemHandler inventory = getInventoryFromLink(itemHandler.getStackInSlot(i));
                if (inventory != null) {
                    noLink = false;
                    if (findTokenInItemHandler(inventory)) {
                        return;
                    }
                }
            }
            if (noLink) {
                //we don't have any linkcard to a permanenet inventory
                setMaschineState(MaschineState.PROBLEM);
            }
        }
    }

    /**
     * @return if a Token valid has been found
     */
    private boolean findTokenInItemHandler(IItemHandler items) {
        final int slotCount = items.getSlots();
        ItemStack itemStack;
        for (int i = 0; i < slotCount; i++) {
            itemStack = items.getStackInSlot(i);
            ICraftingRequest craftingRequest;
            if ((!itemStack.isEmpty()) && (craftingRequest = itemStack.getCapability(APICapabilities
                    .CAPABILITY_CRAFTING_REQUEST, null)) != null) {
                if (!craftingRequest.isCompleted()) {
                    itemStack = items.extractItem(i, 1, false);
                    if (!itemStack.isEmpty()) {
                        token = itemStack.copy();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setMaschineState(@Nonnull MaschineState state) {
        maschineState = state;
        if (hasWorld()) {
            if (!getWorld().isRemote) {
                //send update to client
                MBMAPacketHandler.INSTANCE.sendToDimension(new MaschineStateUpdatePacket(getPos(), state), world.provider.getDimension());
            } else {
                getWorld().markBlockRangeForRenderUpdate(pos, pos);
            }
        }
        markDirty();
    }


    public void destroyed() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty())
                getWorld().spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i).copy()));
        }
        //TODO empty TSI storage's and drop token
    }
}
