package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.api.MBMAProperties;
import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.enums.MaschineState;
import de.webtwob.mbma.common.MBMAPacketHandler;
import de.webtwob.mbma.common.capability.CombinedItemHandler;
import de.webtwob.mbma.common.capability.FilteredItemHandler;
import de.webtwob.mbma.common.interfaces.ICondition;
import de.webtwob.mbma.common.interfaces.IMaschineState;
import de.webtwob.mbma.common.inventory.MBMAFilter;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static de.webtwob.mbma.common.references.MBMA_NBTKeys.QS_STATE;
import static de.webtwob.mbma.common.references.MBMA_NBTKeys.QS_TOKEN;

/**
 * Created by bennet on 17.03.17.
 */
public class QSTileEntity extends TileEntity implements ITickable, IMaschineState {

    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> ITEM_HANDLER      = null;
    private final  NonNullList<ItemStack>   permLinkList      = NonNullList.withSize(6, ItemStack.EMPTY);
    private final  NonNullList<ItemStack>   tempLinkList      = NonNullList.withSize(6, ItemStack.EMPTY);
    private final  NonNullList<ItemStack>   recipeList        = NonNullList.withSize(6, ItemStack.EMPTY);
    private final  NonNullList<ItemStack>   internalList      = NonNullList.withSize(9, ItemStack.EMPTY);
    private final  ItemStackHandler         permanentLinks    = new FilteredItemHandler(permLinkList, MBMAFilter
                                                                                                              .LINK_FILTER, 1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final  ItemStackHandler         temporaryLinks    = new FilteredItemHandler(tempLinkList, MBMAFilter
                                                                                                              .LINK_FILTER, 1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final  ItemStackHandler         recipeLinks       = new FilteredItemHandler(recipeList, MBMAFilter
                                                                                                            .LINK_FILTER, 1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final  ItemStackHandler         internalInventory = new ItemStackHandler(internalList);
    private final  CombinedItemHandler      combinedLinks     = new CombinedItemHandler(permanentLinks,
                                                                                        temporaryLinks, recipeLinks);
    @Nonnull
    private        ItemStack                token             = ItemStack.EMPTY;
    @Nonnull
    private        MaschineState            maschineState     = MaschineState.PROBLEM;
    @Nonnull
    private        List<String>             errorMessage      = new ArrayList<>();
    private final  ICondition               IS_LINKED         = () -> {

        boolean noPermanentStorage = true;
        boolean noTemporaryStorage = true;
        boolean noRecipeBank       = true;
        for(int i = 0; (noTemporaryStorage || noRecipeBank || noPermanentStorage) && i < 6; i++) {
            if(!combinedLinks.getStackInSlot(i).isEmpty()) {
                noPermanentStorage = false;
            }
            if(!combinedLinks.getStackInSlot(i + 6).isEmpty()) {
                noTemporaryStorage = false;
            }
            if(!combinedLinks.getStackInSlot(i + 12).isEmpty()) {
                noRecipeBank = false;
            }
        }

        if(noPermanentStorage) { errorMessage.add("text.mbma:qs.error.permmissing"); }
        if(noTemporaryStorage) { errorMessage.add("text.mbma:qs.error.tempmissing"); }
        if(noRecipeBank) { errorMessage.add("text.mbma:qs.error.recipebank"); }

        return noPermanentStorage || noTemporaryStorage || noRecipeBank;

    };
    private        ICondition               errorCondition    = () -> false;
    private        int                      idleTimer         = 0;

    @Nonnull
    public List<String> getErrorMessages() {
        return errorMessage;
    }

    @Nullable
    private IItemHandler getInventoryFromLink(ItemStack itemStack) {
        BlockPos pos   = getLinkFromItemStack(itemStack);
        World    world = getWorld();
        if(pos != null) {
            IBlockState state = world.getBlockState(pos);

            if(!state.getProperties().containsKey(MBMAProperties.CONNECTED)) { return null; }
            if(state.getValue(MBMAProperties.CONNECTED)) {
                EnumFacing dir = state.getValue(MBMAProperties.FACING);
                TileEntity te  = world.getTileEntity(pos.offset(dir));
                if(te != null) {
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
        ITEM_HANDLER.getStorage().readNBT(ITEM_HANDLER, combinedLinks, null, td.getTag(MBMA_NBTKeys.QS_ITEM_HANDLER));
        ITEM_HANDLER.getStorage().readNBT(ITEM_HANDLER, internalInventory, null, td.getTag(MBMA_NBTKeys
                                                                                                   .QS_ITEM_HANDLER_INTERN));
        setMaschineState(MaschineState.values()[td.getInteger(QS_STATE)]);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound td                = getTileData();
        NBTBase        itemHandler       = ITEM_HANDLER.getStorage().writeNBT(ITEM_HANDLER, this.combinedLinks, null);
        NBTBase        itemHandlerIntern = ITEM_HANDLER.getStorage().writeNBT(ITEM_HANDLER, this.internalInventory,
                                                                              null);
        if(itemHandler != null) {
            td.setTag(MBMA_NBTKeys.QS_ITEM_HANDLER, itemHandler);
        }
        if(itemHandlerIntern != null) {
            td.setTag(MBMA_NBTKeys.QS_ITEM_HANDLER_INTERN, itemHandlerIntern);
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
        if(ITEM_HANDLER != null && capability == ITEM_HANDLER) {
            return (T) combinedLinks;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            if(idleTimer < 0) {
                switch(getMaschineState()) {
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
    }

    private void runIdleTasks() {
        findNewToken();
        if(!token.isEmpty()) {
            setMaschineState(MaschineState.RUNNING);
        }
        idleTimer = 20;
    }

    private void runRunningTask() {
        ICraftingRequest request = getRequestFromToken(token);
        if(request == null || request.isCompleted()) {
            //TODO clear Temp inventory content
            for(int slot = 0; slot < internalList.size(); slot++) {
                internalList.set(slot, moveStackToPermanent(internalList.get(slot)));
            }

            returnEmptyToken();
            if(token.isEmpty() && isInternalEmpty()) {
                setMaschineState(MaschineState.IDLE);
            }
        } else {
            //TODO handle request
            //work on the current token
        }
    }

    private void runWaitingTask() {

    }

    private void runProblemTask() {

        /* check if error still persists if that is the case wait else continue */
        if(errorCondition.checkCondition()) {
            idleTimer = 20;
        } else {
            setMaschineState(MaschineState.IDLE);
        }
    }

    private boolean isInternalEmpty() {
        for(ItemStack itemStack : internalList) {
            if(!itemStack.isEmpty()) { return false; }
        }
        return true;
    }

    /**
     * @return what could not be moved or ItemStack.EMPTY
     */
    private ItemStack moveStackToPermanent(ItemStack itemStack) {
        IItemHandler inv;
        for(int linkID = 0; linkID < permanentLinks.getSlots(); linkID++) {
            inv = getInventoryFromLink(permanentLinks.getStackInSlot(linkID));
            if(inv != null) {
                itemStack = moveStackToInventory(itemStack, inv);
            }
            if(itemStack.isEmpty()) { break; }
        }
        return itemStack;
    }

    /**
     * @return what could not be moved or ItemStack.EMPTY
     */
    @Nonnull
    private ItemStack moveStackToInventory(@Nonnull ItemStack stack, @Nonnull IItemHandler inv) {
        for(int slot = 0; slot < inv.getSlots(); slot++) {
            stack = inv.insertItem(slot, stack, false);
            if(stack.isEmpty()) { break; }
        }
        return stack;
    }

    private static BlockPos getLinkFromItemStack(ItemStack itemStack) {
        IBlockPosProvider bpp = itemStack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null);
        return bpp != null ? bpp.getBlockPos() : null;
    }

    private void findNewToken() {
        if(!world.isRemote) {
            if(token.isEmpty()) {
                boolean noLink = true;
                //iterate over the Linkcard for Permanent Storage Interfaces
                for(int i = 0; i < permanentLinks.getSlots(); i++) {
                    IItemHandler inventory = getInventoryFromLink(permanentLinks.getStackInSlot(i));
                    if(inventory != null) {
                        noLink = false;
                        if(findTokenInItemHandler(inventory)) {
                            return;
                        }
                    }
                }
                if(noLink) {
                    //we don't have any linkcard to a permanent inventory
                    setMaschineState(MaschineState.PROBLEM);
                    errorCondition = IS_LINKED;
                }
            } else {
                setMaschineState(MaschineState.RUNNING);
            }
        }
    }

    /**
     * @return if a Token valid has been found
     */
    private boolean findTokenInItemHandler(IItemHandler items) {
        final int slotCount = items.getSlots();
        ItemStack itemStack;
        for(int i = 0; i < slotCount; i++) {
            itemStack = items.getStackInSlot(i);
            ICraftingRequest craftingRequest = getRequestFromToken(itemStack);
            if(craftingRequest != null && !craftingRequest.isCompleted()) {
                ItemStack tokenExtract = items.extractItem(i, 1, false);
                if(!tokenExtract.isEmpty()) {
                    token = tokenExtract.copy();
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    private ICraftingRequest getRequestFromToken(@Nullable ItemStack stack) {
        return !(stack == null || stack.isEmpty()) ? stack.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST,
                                                                         null) : null;
    }

    public void destroyed() {
        //drop linkcards
        for(int i = 0; i < combinedLinks.getSlots(); i++) {
            if(!combinedLinks.getStackInSlot(i).isEmpty()) {
                getWorld().spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), combinedLinks
                                                                                                         .getStackInSlot(i).copy()));
            }
        }
        //drop token
        if(!token.isEmpty()) {
            getWorld().spawnEntity(new EntityItem(world, getPos().getX(), getPos().getY(), getPos().getZ(), token.copy()));
        }
    }

    private void returnEmptyToken() {
        if(!token.isEmpty()) {
            ICraftingRequest req = getRequestFromToken(token);
            if(req != null && !req.isCompleted()) {
                return;
            }
            if(moveStackToPermanent(token).isEmpty()) { this.token = ItemStack.EMPTY; }
        }
    }

    public void creativeComplete() {
        ICraftingRequest request = getRequestFromToken(token);
        if(request != null && !request.isCompleted()) {
            request.setRequest(moveStackToInventory(request.getRequest(), internalInventory));
        }
    }

    @Nonnull
    public MaschineState getMaschineState() {
        return maschineState;
    }


    public void setMaschineState(@Nonnull MaschineState state) {
        maschineState = state;
        if(hasWorld()) {
            if(!getWorld().isRemote) {
                MBMAPacketHandler.INSTANCE.sendToDimension(new MaschineStateUpdatePacket(getPos(), state), world.provider.getDimension());
            } else {
                getWorld().markBlockRangeForRenderUpdate(pos, pos);
            }
        }
        markDirty();
    }


}
