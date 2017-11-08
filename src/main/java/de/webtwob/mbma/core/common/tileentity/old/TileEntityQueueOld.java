package de.webtwob.mbma.core.common.tileentity.old;

import de.webtwob.mbma.api.enums.MachineState;
import de.webtwob.mbma.api.interfaces.block.IMachineState;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.core.common.crafting.old.QSCraftingAccessor;
import de.webtwob.mbma.core.common.crafting.old.QSData;
import de.webtwob.mbma.core.common.packet.MaschineStateUpdatePacket;
import de.webtwob.mbma.core.common.registration.MBMAPacketHandler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static de.webtwob.mbma.core.common.references.MBMA_NBTKeys.QS_DATA;

/**
 * Created by bennet on 17.03.17.
 */
@SuppressWarnings("PointlessBooleanExpression")
@Deprecated
public class TileEntityQueueOld extends TileEntity implements ITickable, IMachineState {

    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> ITEM_HANDLER = null;
    private final QSData data = new QSData(this);

    @Nullable
    private QSCraftingAccessor craftingAccessor;

    @Nonnull
    private List<String> errorMessage = new ArrayList<>();

    @SuppressWarnings("ConstantConditions")
    private final Runnable IS_LINKED = () -> {

        if (noInventory(data.permLinks)) {
            errorMessage.add("text.mbmacore:qs.error.permmissing");
        }
        if (noInventory(data.tempLinks)) {
            errorMessage.add("text.mbmacore:qs.error.tempmissing");
        }
        //TODO implement the check
        if (false && containsItem(data.recipeList)) {
            errorMessage.add("text.mbmacore:qs.error.recipebank");
        }
    };
    private MachineState problemReturn = MachineState.IDLE;
    private Runnable errorCondition = () -> {
    };
    private int idleTimer = 0;

    /**
     * @return true if there is no LinkCard in the ItemStackHandler that links to an Inventory
     */
    private boolean noInventory(ItemStackHandler links) {
        for (int i = 0; i < links.getSlots(); i++) {
            IItemHandler inventory = data.getInventoryFromLink(links.getStackInSlot(i), getWorld());
            if (inventory != null) {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    public List<String> getErrorMessages() {
        return errorMessage;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound td = getTileData();
        if (td.hasKey(QS_DATA)) {
            data.deserializeNBT(td.getTag(QS_DATA));
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound td = getTileData();
        td.setTag(QS_DATA, data.serializeNBT());
        compound = super.writeToNBT(compound);
        return compound;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), writeToNBT(new NBTTagCompound()));
    }


    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return super.getUpdateTag();
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
            return (T) data.combinedLinks;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (idleTimer < 0) {
                switch (getMachineState()) {
                    case IDLE:
                        runIdleTasks();
                        break;
                    case RUNNING:
                        break;
                    case WAITING:
                        break;
                    case PROBLEM:
                        runProblemTask();
                        break;
                }
            } else {
                idleTimer--;
            }
        }
    }

    private void runIdleTasks() {
        findNewToken();
        if (!data.token.isEmpty()) {
            setMachineState(MachineState.RUNNING);
        }
        idleTimer = 20;
    }

    /**
     * When the QS is in this state it requires user attention
     * every 20 Ticks it will check if the problem has been resolved
     * if it has it will set the state to the one designated by @var problemReturn
     */
    private void runProblemTask() {
        /* check if error still persists if that is the case wait else continue */
        errorMessage.clear();
        errorCondition.run();
        if (errorMessage.isEmpty()) {
            setMachineState(problemReturn);
            problemReturn = MachineState.IDLE;
            errorCondition = () -> {
            };
        } else {
            idleTimer = 20;
        }
    }

    private void findNewToken() {
        if (!world.isRemote) {
            if (data.token.isEmpty()) {
                boolean noLink = true;
                //iterate over the Linkcard for Permanent Storage Interfaces
                for (int i = 0; i < data.permLinks.getSlots(); i++) {
                    IItemHandler inventory = data.getInventoryFromLink(data.permLinks.getStackInSlot(i), getWorld());
                    if (inventory != null) {
                        noLink = false;
                        if (findTokenInItemHandler(inventory)) {
                            return;
                        }
                    }
                }
                if (noLink) {
                    //we don't have any linkcard to a permanent inventory
                    setMachineState(MachineState.PROBLEM);
                    errorCondition = IS_LINKED;
                }
            } else {
                setMachineState(MachineState.RUNNING);
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
            ICraftingRequest craftingRequest = ICraftingRequest.getCraftingRequest(itemStack);
            if (craftingRequest != null && !craftingRequest.isCompleted()) {
                ItemStack tokenExtract = items.extractItem(i, 1, false);
                if (!tokenExtract.isEmpty()) {
                    data.token = tokenExtract.copy();
                    return true;
                }
            }
        }
        return false;
    }

    public void destroyed() {
        //drop linkcards
        for (int i = 0; i < data.combinedLinks.getSlots(); i++) {
            if (!data.combinedLinks.getStackInSlot(i).isEmpty()) {
                getWorld().spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(),
                        data.combinedLinks.getStackInSlot(i).copy()
                ));
            }
        }

        //drop gathered Items
        while (!data.collectList.isEmpty()) {
            getWorld().spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), data.collectList.remove(0)));
        }

        //drop token
        if (!data.token.isEmpty()) {
            getWorld().spawnEntity(new EntityItem(world, getPos().getX(), getPos().getY(), getPos().getZ(), data.token));
        }
    }

    private boolean containsItem(NonNullList<ItemStack> list) {
        return !list.stream().allMatch(ItemStack::isEmpty);
    }

    @Nonnull
    public MachineState getMachineState() {
        return data.machineState;
    }

    public void setMachineState(@Nonnull MachineState state) {
        data.machineState = state;
        if (hasWorld()) {
            if (!getWorld().isRemote) {
                MBMAPacketHandler.INSTANCE.sendToDimension(
                        new MaschineStateUpdatePacket(getPos(), state), world.provider.getDimension());
            } else {
                getWorld().markBlockRangeForRenderUpdate(pos, pos);
            }
        }
        markDirty();
    }

    public ICraftingRequest getCurrentRequest() {
        return craftingAccessor != null ? craftingAccessor.getRequest() : null;
    }

    public String getCraftingStatus() {
        return craftingAccessor != null ? craftingAccessor.getRequest().getRequest().toString() : "-";
    }
}
