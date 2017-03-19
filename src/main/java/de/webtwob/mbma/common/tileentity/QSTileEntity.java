package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.api.MBMAProperties;
import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import de.webtwob.mbma.api.enums.MaschineState;
import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.capability.QSItemHandler;
import de.webtwob.mbma.common.item.MBMAItemList;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
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

import static de.webtwob.mbma.common.references.MBMA_NBTKeys.QS_TOKEN;

/**
 * Created by bennet on 17.03.17.
 */
public class QSTileEntity extends TileEntity implements ITickable {

    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> ITEM_HANDLER = null;

    private final QSItemHandler itemHandler = new QSItemHandler(this);
    @Nonnull
    private       ItemStack     token       = ItemStack.EMPTY;
    private       int           idleTimer   = 0;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound td = getTileData();
        token = new ItemStack(td.getCompoundTag(QS_TOKEN));
        ITEM_HANDLER.getStorage().readNBT(ITEM_HANDLER, itemHandler, null, td.getTag(MBMA_NBTKeys.QS_ITEM_HANDLER));
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound td          = getTileData();
        NBTBase        itemhandler = ITEM_HANDLER.getStorage().writeNBT(ITEM_HANDLER, itemHandler, null);
        if(itemhandler != null) { td.setTag(MBMA_NBTKeys.QS_ITEM_HANDLER, itemhandler); }
        td.setTag(QS_TOKEN, token.serializeNBT());
        compound = super.writeToNBT(compound);
        return compound;
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

    public MaschineState getMaschineState() {
        if(token.isEmpty()) { return MaschineState.IDLE; }
        return MaschineState.PROBLEM;
    }

    @Deprecated
    public boolean isIdle() {
        return token.isEmpty();
    }

    @Override
    public void update() {
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

    private void runRunningTask() {

    }

    private void runIdleTasks() {
        if(idleTimer > 0) {
            idleTimer--;
            return;
        }
        findNewToken();
        if(!token.isEmpty()) {
            updateBlockState();
            return;
        }
        idleTimer = 20;
    }

    private void updateBlockState() {
        World world;
        //noinspection ConstantConditions
        if((world = getWorld()) != null) {
            world.setBlockState(getPos(), world.getBlockState(getPos()).withProperty(MBMAProperties.STATE,
                                                                                     getMaschineState()), 2);
        }
    }

    private BlockPos getLinkFromItemStack(ItemStack itemStack) {
        IBlockPosProvider bpp = itemStack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null);
        return bpp != null ? bpp.getBlockPos() : null;
    }

    private void findNewToken() {
        if(hasWorld()) {
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
                    //check if PSI is connected
                    if(state.getValue(MBMAProperties.CONNECTED)) {
                        dir = state.getValue(MBMAProperties.FACING);
                        pos = pos.offset(dir);
                        tileEntity = getWorld().getTileEntity(pos);
                        //check and get the ItemHandler Capibility
                        if(tileEntity != null && null != (items = tileEntity.getCapability(CapabilityItemHandler
                                                                                                   .ITEM_HANDLER_CAPABILITY, dir.getOpposite()))) {
                            //search for a token in the IItemHandlers Slots
                            if(findTokenInItemHandler(items)) {
                                MBMALog.debug("Found Token in QueueStack at {} {} {}", getPos().getX(), getPos().getY(), getPos().getZ());
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
