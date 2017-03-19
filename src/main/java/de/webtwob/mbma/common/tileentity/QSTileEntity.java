package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.api.MBMAProperties;
import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import de.webtwob.mbma.api.enums.MaschineState;
import de.webtwob.mbma.common.MBMAPacketHandler;
import de.webtwob.mbma.common.capability.QSItemHandler;
import de.webtwob.mbma.common.interfaces.IMaschineState;
import de.webtwob.mbma.common.item.MBMAItemList;
import de.webtwob.mbma.common.packet.MaschineStateUpdatePacket;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mbma.common.references.MBMA_NBTKeys.QS_STATE;
import static de.webtwob.mbma.common.references.MBMA_NBTKeys.QS_TOKEN;

/**
 * Created by bennet on 17.03.17.
 */
public class QSTileEntity extends TileEntity implements ITickable, IMaschineState {

    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> ITEM_HANDLER = null;

    private final QSItemHandler itemHandler   = new QSItemHandler(this);
    @Nonnull
    private       ItemStack     token         = ItemStack.EMPTY;
    private       int           idleTimer     = 0;
    private       MaschineState maschineState = MaschineState.IDLE;

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
        NBTTagCompound td          = getTileData();
        NBTBase        itemhandler = ITEM_HANDLER.getStorage().writeNBT(ITEM_HANDLER, itemHandler, null);
        if(itemhandler != null) { td.setTag(MBMA_NBTKeys.QS_ITEM_HANDLER, itemhandler); }
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
        if(ITEM_HANDLER != null && capability == ITEM_HANDLER) {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            switch(getMaschineState()) {
                case IDLE:
                    runIdleTasks();
                    break;
                case RUNNING:
                    runRunningTask();
                    break;
                case WAITING:
                    break;
                case PROBLEM:
                    break;
            }
        }

    }

    private void runRunningTask() {

    }    @Nonnull
    public MaschineState getMaschineState() {
        return maschineState;
    }

    private void runIdleTasks() {
        if(idleTimer > 0) {
            idleTimer--;
            return;
        }
        findNewToken();
        if(!token.isEmpty()) {
            setMaschineState(MaschineState.RUNNING);
            return;
        }
        idleTimer = 20;
    }

    private BlockPos getLinkFromItemStack(ItemStack itemStack) {
        IBlockPosProvider bpp = itemStack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null);
        return bpp != null ? bpp.getBlockPos() : null;
    }    public void setMaschineState(@Nonnull MaschineState state) {
        maschineState = state;
        if(hasWorld()) {
            if(!getWorld().isRemote) {
                MBMAPacketHandler.INSTANCE.sendToDimension(new MaschineStateUpdatePacket(getPos(), state), world.provider.getDimension());
                //send update to client
            } else {
                getWorld().markBlockRangeForRenderUpdate(pos, pos);
            }
        }
        markDirty();
    }

    private void findNewToken() {
        if(!world.isRemote) {
            BlockPos     pos;
            EnumFacing   dir;
            TileEntity   tileEntity;
            IBlockState  state;
            IItemHandler items;
            //itterate over the Linkcard for Permanent Storage Interfaces
            for(int i = 0; i < 6; i++) {

                //when a card is present
                pos = getLinkFromItemStack(itemHandler.getStackInSlot(i));
                if(pos != null) {
                    state = getWorld().getBlockState(pos);
                    state = state.getBlock().getActualState(state, world, pos);

                    //check if PSI is connected (ATM not checking if it is actually a PSI)
                    if(state.getPropertyKeys().contains(MBMAProperties.CONNECTED) && state.getValue(MBMAProperties
                                                                                                            .CONNECTED)) {
                        dir = state.getValue(MBMAProperties.FACING);
                        pos = pos.offset(dir);
                        tileEntity = getWorld().getTileEntity(pos);

                        //check and get the ItemHandler Capibility
                        if(tileEntity != null && null != (items = tileEntity.getCapability(CapabilityItemHandler
                                                                                                   .ITEM_HANDLER_CAPABILITY, dir.getOpposite()))) {
                            //search for a token in the IItemHandlers Slots
                            if(findTokenInItemHandler(items)) {
                                return;
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * @return if a Token has been found
     */
    private boolean findTokenInItemHandler(IItemHandler items) {
        final int slotCount = items.getSlots();
        ItemStack itemStack;
        for(int i = 0; i < slotCount; i++) {
            itemStack = items.getStackInSlot(i);
            if((!itemStack.isEmpty()) && (itemStack.getItem() == MBMAItemList.TOKEN)) {
                itemStack = items.extractItem(i, 1, false);
                if(!itemStack.isEmpty()) {
                    token = itemStack.copy();
                    return true;
                }
            }
        }
        return false;
    }






}
