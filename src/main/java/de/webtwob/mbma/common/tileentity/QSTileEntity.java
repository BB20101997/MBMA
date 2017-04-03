package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.api.MBMAProperties;
import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.implementations.CombinedItemHandler;
import de.webtwob.mbma.api.capability.implementations.FilteredItemHandler;
import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRecipe;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.enums.MaschineState;
import de.webtwob.mbma.api.interfaces.ICondition;
import de.webtwob.mbma.api.interfaces.IMaschineState;
import de.webtwob.mbma.common.MBMAPacketHandler;
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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static de.webtwob.mbma.common.references.MBMA_NBTKeys.QS_STATE;
import static de.webtwob.mbma.common.references.MBMA_NBTKeys.QS_TOKEN;

/**
 * Created by bennet on 17.03.17.
 */
public class QSTileEntity extends TileEntity implements ITickable, IMaschineState {
    
    private static final String QS_COLLECT_LENGTH = MultiblockMaschineAutomation.MODID + ":qsCollectLength";
    private static final String QS_COLLECT_LIST   = MultiblockMaschineAutomation.MODID + ":qsCollectList";
    
    enum RunStages {
        INIT, SEARCH, COLLECT, CRAFT, RETURN
    }
    
    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> ITEM_HANDLER = null;
    private final  NonNullList<ItemStack>   permLinkList = NonNullList.withSize(6, ItemStack.EMPTY);
    private final  NonNullList<ItemStack>   tempLinkList = NonNullList.withSize(6, ItemStack.EMPTY);
    private final  NonNullList<ItemStack>   recipeList   = NonNullList.withSize(6, ItemStack.EMPTY);
    private final  NonNullList<ItemStack>   collectList  = NonNullList.create();
    
    private final ItemStackHandler permanentLinks = new FilteredItemHandler(permLinkList, MBMAFilter.LINK_FILTER, 1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final ItemStackHandler temporaryLinks = new FilteredItemHandler(tempLinkList, MBMAFilter.LINK_FILTER, 1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private final ItemStackHandler recipeLinks    = new FilteredItemHandler(recipeList, MBMAFilter.LINK_FILTER, 1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    
    private final CombinedItemHandler      combinedLinks                  = new CombinedItemHandler(permanentLinks,
                                                                                                    temporaryLinks,
                                                                                                    recipeLinks
    );
    @Nonnull
    private       ItemStack                token                          = ItemStack.EMPTY;
    private       RunStages                stage                          = RunStages.SEARCH;
    @Nonnull
    private       MaschineState            maschineState                  = MaschineState.PROBLEM;
    @Nonnull
    private       List<String>             errorMessage                   = new ArrayList<>();
    private       boolean                  creativeCompleteCurrentRequest = false;
    private       LinkedList<RequestTuple> recipes                        = new LinkedList<>();
    private       MaschineState            problemReturn                  = MaschineState.IDLE;
    private final ICondition               IS_LINKED                      = () -> {
        
        boolean noPermanentStorage = true;
        boolean noTemporaryStorage = true;
        boolean noRecipeBank       = true;
        for(int i = 0; (noTemporaryStorage || noRecipeBank || noPermanentStorage) && i < 6; i++){
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
        
        if(noPermanentStorage) {
            errorMessage.add("text.mbma:qs.error.permmissing");
        }
        if(noTemporaryStorage) {
            errorMessage.add("text.mbma:qs.error.tempmissing");
        }
        if(noRecipeBank) {
            errorMessage.add("text.mbma:qs.error.recipebank");
        }
        
        return noPermanentStorage || noTemporaryStorage || noRecipeBank;
        
    };
    private       ICondition               errorCondition                 = MBMAFilter.FALSE;
    private       int                      idleTimer                      = 0;
    
    private class RequestTuple {
        
        ICraftingRequest request;
        ICraftingRecipe  recipe;
    }
    
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
            
            if(!state.getProperties().containsKey(MBMAProperties.CONNECTED)) {
                return null;
            }
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
        int size = td.getInteger(QS_COLLECT_LENGTH);
        while(collectList.size() < size){
            collectList.add(ItemStack.EMPTY);
        }
        new ItemStackHandler(collectList).deserializeNBT(td.getCompoundTag(QS_COLLECT_LIST));
        setMaschineState(MaschineState.values()[td.getInteger(QS_STATE)]);
    }
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound td          = getTileData();
        NBTBase        itemHandler = ITEM_HANDLER.getStorage().writeNBT(ITEM_HANDLER, this.combinedLinks, null);
        if(itemHandler != null) {
            td.setTag(MBMA_NBTKeys.QS_ITEM_HANDLER, itemHandler);
        }
        td.setTag(QS_TOKEN, token.serializeNBT());
        td.setInteger(QS_STATE, maschineState.ordinal());
        int     leght = collectList.size();
        NBTBase base  = new ItemStackHandler(collectList).serializeNBT();
        td.setTag(QS_COLLECT_LIST, base);
        td.setInteger(QS_COLLECT_LENGTH, leght);
        
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
    
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(ITEM_HANDLER != null && capability == ITEM_HANDLER) {
            //noinspection unchecked
            return (T) combinedLinks;
        }
        return super.getCapability(capability, facing);
    }
    
    @Override
    public void update() {
        if(!world.isRemote) {
            if(idleTimer < 0) {
                switch(getMaschineState()){
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
    
    private void runRunningTask() {
        switch(stage){
            case INIT:
                initRequest();
                break;
            case SEARCH:
                searchFroRecipe();
                break;
            case COLLECT:
                collectItemsForRecipe();
                break;
            case CRAFT:
                craftRecipe();
                break;
            case RETURN:
                returnResults();
                break;
        }
    }
    
    private void initRequest() {
        RequestTuple tuple = new RequestTuple();
        tuple.request = getRequestFromToken(token);
        stage = RunStages.SEARCH;
    }
    
    private void returnResults() {
        clearCollect();
        clearTemp();
        returnEmptyToken();
        if(isCollectEmpty()) {
            if(token.isEmpty()) {
                stage = RunStages.INIT;
                setMaschineState(MaschineState.IDLE);
            }
        }
    }
    
    private void clearCollect() {
        ItemStack ret;
        while(!collectList.isEmpty()){
            ret = collectList.remove(0);
            ret = moveStackToPermanent(ret);
            if(!ret.isEmpty()) {
                collectList.add(ret);
                return;
            }
        }
    }
    
    private boolean isCollectEmpty() {
        return collectList.isEmpty();
    }
    
    private void craftRecipe() {
        if(recipes.isEmpty()) {
            stage = RunStages.RETURN;
            return;
        }
        RequestTuple tuple = recipes.getLast();
        if(creativeCompleteCurrentRequest) {
            //creativeComplete is active !CHEATING! current step
            ItemStack stack = tuple.request.getRequest();
            stack = stack.copy();
            stack.setCount(tuple.request.getQuantity());
            stack = moveStackToPermanent(stack);
            tuple.request.setQuantity(stack.getCount());
            if(tuple.request.isCompleted()) {
                creativeCompleteCurrentRequest = false;
            }
        } else {
            //TODO
            //check if collect contains all requirements else return to COLLECT
            //reserve maschine
            //push collected Items to Maschine
            //wait for result
            //decrement request
        }
        if(tuple.request.isCompleted()) {
            recipes.removeLast();
            if(recipes.isEmpty()){
                stage = RunStages.RETURN;
            }else{
                stage = RunStages.COLLECT;
            }
        }
    }
    
    private void collectItemsForRecipe() {
        if(creativeCompleteCurrentRequest) {
            //if creativeComplete is active we skip to CRAFT
            stage = RunStages.CRAFT;
        }
        if(recipes.isEmpty()) {
            //no request left therefor we RETURN
            stage = RunStages.RETURN;
            return;
        }
        RequestTuple tuple = recipes.getLast();
        if(tuple.recipe == null) {
            //the request has no recipe set need to find one first
            stage = RunStages.SEARCH;
            return;
        }
        
        //get a copy of the requirements
        final List<ItemStack> requirements = new ArrayList<>();
        tuple.recipe.getRequirements().forEach(e -> requirements.add(e.copy()));
    
        //get every matching item we can find
        Stream.concat(tempLinkList.stream(),permLinkList.stream()).map(this::getInventoryFromLink).filter(Objects::nonNull).forEach((e) -> {
            for(int i = 0; i < e.getSlots(); i++){
                final int       slot  = i;
                final ItemStack stack = e.getStackInSlot(slot);
                requirements.stream().filter(s -> !s.isEmpty()).forEach(s -> {
                    if(ItemHandlerHelper.canItemStacksStack(s, stack)) {
                        ItemStack extract = e.extractItem(slot, s.getCount(), false);
                        s.shrink(extract.getCount());
                        ItemStack result = ItemHandlerHelper.insertItem(
                                new ItemStackHandler(collectList), extract, false);
                        if(!result.isEmpty()) {
                            collectList.add(result);
                        }
                    }
                });
                requirements.removeIf(ItemStack::isEmpty);
            }
        });
        
        //could not get all requirements creating sub-request
        if(requirements.size() > 0) {
            RequestTuple request = new RequestTuple();
            request.request = APICapabilities.CAPABILITY_CRAFTING_REQUEST.getDefaultInstance();
            ItemStack reqItem = requirements.get(0);
            if(request.request != null && !reqItem.isEmpty()) {
                request.request.setRequest(reqItem);
                request.request.setQuantity(reqItem.getCount());
                recipes.addFirst(request);
            }
            //move all gathered to temp
            moveCollectToTemp();
            if(!isCollectEmpty()) {
                //failed to move all to temp
                errorCondition = () -> {
                    moveCollectToTemp();
                    return !isCollectEmpty();
                };
                errorMessage.add("text.error.qs.temp.full");
                setMaschineState(MaschineState.PROBLEM);
            }
        } else {
            //got all requirements can craft now
            stage = RunStages.CRAFT;
        }
    }
    
    private void moveCollectToTemp() {
        collectList.forEach(e -> {
            ItemStack ret = moveStackToTemp(e);
            if(!ret.isEmpty()) {
                e.setCount(ret.getCount());
            }
        });
        //remove all empty ItemStacks
        collectList.removeIf(ItemStack::isEmpty);
    }
    
    private void searchFroRecipe() {
        if(creativeCompleteCurrentRequest) {
            stage = RunStages.CRAFT;
        }
        //TODO
        //find a recipe that produces the right result
        //than move to COLLECT
    }
    
    private void runIdleTasks() {
        findNewToken();
        if(!token.isEmpty()) {
            setMaschineState(MaschineState.RUNNING);
        }
        idleTimer = 20;
    }
    
    private void clearTemp() {
        tempLinkList.stream().map(this::getInventoryFromLink).filter(Objects::nonNull).forEach(e -> {
            for(int i = 0; i < e.getSlots(); i++){
                
                ItemStack stack = moveStackToPermanent(e.extractItem(i, e.getStackInSlot(i).getCount(), false));
                if(!stack.isEmpty()) {
                    stack = ItemHandlerHelper.insertItem(new ItemStackHandler(collectList), stack, false);
                    if(!stack.isEmpty()) {
                        collectList.add(stack);
                    }
                }
            }
        });
    }
    
    private void runWaitingTask() {
    
    }
    
    /**
     * When the QS is in this state it requires user attention
     * every 20 Ticks it will check if the problem has been resolved
     * if it has it will set the state to the one designated by @var problemReturn
     */
    private void runProblemTask() {
        /* check if error still persists if that is the case wait else continue */
        if(errorCondition.checkCondition()) {
            idleTimer = 20;
        } else {
            setMaschineState(problemReturn);
            errorMessage.clear();
            problemReturn = MaschineState.IDLE;
            errorCondition = MBMAFilter.FALSE;
        }
    }
    
    /**
     * @return what could not be moved or ItemStack.EMPTY
     */
    private ItemStack moveStackToPermanent(ItemStack itemStack) {
        IItemHandler inv;
        for(int linkID = 0; linkID < permanentLinks.getSlots(); linkID++){
            inv = getInventoryFromLink(permanentLinks.getStackInSlot(linkID));
            if(inv != null) {
                itemStack = ItemHandlerHelper.insertItemStacked(inv, itemStack, false);
            }
            if(itemStack.isEmpty()) {
                break;
            }
        }
        return itemStack;
    }
    
    private ItemStack moveStackToTemp(ItemStack itemStack) {
        IItemHandler inv;
        for(int linkID = 0; linkID < temporaryLinks.getSlots(); linkID++){
            inv = getInventoryFromLink(temporaryLinks.getStackInSlot(linkID));
            if(inv != null) {
                itemStack = ItemHandlerHelper.insertItemStacked(inv, itemStack, false);
            }
            if(itemStack.isEmpty()) {
                break;
            }
        }
        return itemStack;
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
                for(int i = 0; i < permanentLinks.getSlots(); i++){
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
        for(int i = 0; i < slotCount; i++){
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
                                                                         null
        ) : null;
    }
    
    public void destroyed() {
        //drop linkcards
        for(int i = 0; i < combinedLinks.getSlots(); i++){
            if(!combinedLinks.getStackInSlot(i).isEmpty()) {
                getWorld().spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(),
                                                      combinedLinks.getStackInSlot(i).copy()
                ));
            }
        }
        //drop token
        if(!token.isEmpty()) {
            getWorld().spawnEntity(
                    new EntityItem(world, getPos().getX(), getPos().getY(), getPos().getZ(), token.copy()));
        }
    }
    
    private void returnEmptyToken() {
        if(!token.isEmpty()) {
            ICraftingRequest req = getRequestFromToken(token);
            if(req != null && !req.isCompleted()) {
                return;
            }
            if(moveStackToPermanent(token).isEmpty()) {
                this.token = ItemStack.EMPTY;
            }
        }
    }
    
    public void creativeComplete() {
        ICraftingRequest request = getRequestFromToken(token);
        if(request != null && !request.isCompleted()) {
            creativeCompleteCurrentRequest = true;
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
                MBMAPacketHandler.INSTANCE.sendToDimension(
                        new MaschineStateUpdatePacket(getPos(), state), world.provider.getDimension());
            } else {
                getWorld().markBlockRangeForRenderUpdate(pos, pos);
            }
        }
        markDirty();
    }
    
}
