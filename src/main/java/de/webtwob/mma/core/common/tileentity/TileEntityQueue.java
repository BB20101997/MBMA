package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.crafting.ItemStackContainer;
import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.api.interfaces.capability.ICraftingRequestProvider;
import de.webtwob.mma.api.interfaces.gui.IGUIHandlerBoth;
import de.webtwob.mma.api.interfaces.tileentity.IMultiBlockTile;
import de.webtwob.mma.api.multiblock.MultiBlockGroup;
import de.webtwob.mma.api.multiblock.MultiBlockGroupTypeInstance;
import de.webtwob.mma.api.registries.MultiBlockGroupType;
import de.webtwob.mma.core.client.gui.QueueGui;
import de.webtwob.mma.core.common.config.MMAConfiguration;
import de.webtwob.mma.core.common.inventory.QueueContainer;
import de.webtwob.mma.core.common.multiblockgroups.QueueGroupType;
import de.webtwob.mma.core.common.references.NBTKeys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityQueue extends MultiBlockTileEntity implements IGUIHandlerBoth {
    
    @ObjectHolder("mmacore:queue")
    private static final MultiBlockGroupType MANAGER_QUEUE = null;
    
    private static Capability<IItemHandler> capabilityItemHandler;
    private static Capability<ICraftingRequest> capabilityCraftingRequest;
    private static Capability<ICraftingRequestProvider> capabilityCraftingRequestProvider;
    
    private final LinkedList<ItemStackContainer> freeRequestContainer = new LinkedList<>();
    private final LinkedList<ItemStackContainer> inUseRequestContainer = new LinkedList<>();
    
    private IItemHandler handler = new EnqueueingItemHandler();
    private boolean resync = false;
    
    public TileEntityQueue() {
        //init freeItemStackContainers
        for (int i = 0; i < MMAConfiguration.queueLength; i++) {
            freeRequestContainer.add(createItemStackContainer());
        }
    }
    
    @CapabilityInject(IItemHandler.class)
    private static void injectItemHandler(Capability<IItemHandler> handler) {
        capabilityItemHandler = handler;
    }
    
    @CapabilityInject(ICraftingRequest.class)
    private static void injectCraftingRequest(Capability<ICraftingRequest> handler) {
        capabilityCraftingRequest = handler;
    }
    
    @CapabilityInject(ICraftingRequestProvider.class)
    private static void injectCraftingRequestProvider(Capability<ICraftingRequestProvider> handler) {
        capabilityCraftingRequestProvider = handler;
    }
    
    public MultiBlockGroupType getGroupType() {
        return MANAGER_QUEUE;
    }
    
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing) || getCapability(capability, facing) != null;
    }
    
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (TileEntityQueue.capabilityItemHandler != null && TileEntityQueue.capabilityItemHandler == capability) {
            return (T) handler;
        } else if (capabilityCraftingRequestProvider != null && capabilityCraftingRequestProvider == capability) {
            return (T) (ICraftingRequestProvider) this::groupGetRequestIfRequirementHolds;
        }
        return super.getCapability(capability, facing);
    }
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound supComp = super.writeToNBT(compound);
        NBTTagList items = new NBTTagList();
        inUseRequestContainer.forEach(stack -> items.appendTag(stack.getItemStack().serializeNBT()));
        supComp.setTag(NBTKeys.QUEUE_STACKS, items);
        return supComp;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        removeAllFromQueue(inUseRequestContainer);
        
        inUseRequestContainer.clear();
        int free = MMAConfiguration.queueLength;
        ItemStackContainer container;
        for (NBTBase comp : compound.getTagList(NBTKeys.QUEUE_STACKS, Constants.NBT.TAG_COMPOUND)) {
            container = createItemStackContainer(new ItemStack((NBTTagCompound) comp));
            inUseRequestContainer.add(container);
            free--;
            if (free <= 0) {
                //we are over our limit we keep the request but will not allow to reuse the container
                container.setPoolReturn(this::disposeItemStackContainer);
            }
        }
        addAllToQueue(inUseRequestContainer);
        //adjust freeContainer amount
        while (free > freeRequestContainer.size()) {
            freeRequestContainer.add(createItemStackContainer());
        }
        while (free < freeRequestContainer.size() && !freeRequestContainer.isEmpty()) {
            freeRequestContainer.remove(0);
        }
    }
    
    /**
     * @return an Array containing the currently enqueued Requests
     */
    @Nonnull
    public LinkedList<ItemStackContainer> getCurrentRequests() {
        MultiBlockGroup group = IMultiBlockTile.getGroup(world, pos, getGroupType());
        if (group != null) {
            MultiBlockGroupTypeInstance instance = group.getTypeInstance();
            if (instance instanceof QueueGroupType.Instance) {
                return ((QueueGroupType.Instance) instance).getQueue();
            }
        }
        return new LinkedList<>();
    }
    
    /**
     * find the first Group Member that returns a non empty ItemStack and return said ItemStack
     */
    @Nonnull
    private ItemStack groupGetRequestIfRequirementHolds(@Nonnull final Predicate<ItemStack> requirement) {
        if (group == null) {
            return ItemStack.EMPTY;
        }
        return getCurrentRequests().stream()
                .filter(isc -> requirement.test(isc.getItemStack()))
                .findFirst()
                .map(isc -> {
                    ItemStack request = isc.getItemStack();
                    isc.setItemStack(ItemStack.EMPTY);
                    isc.returnToPool();
                    return request;
                })
                .orElse(ItemStack.EMPTY);
    }
    
    @Override
    public void update() {
        super.update();
        if(resync){
            resync = false;
            removeAllFromQueue(inUseRequestContainer);
            removeAllFromQueue(freeRequestContainer);
            addAllToQueue(inUseRequestContainer);
        }
    }
    
    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        removeAllFromQueue(inUseRequestContainer);
    }
    
    private ItemStackContainer createItemStackContainer() {
        return createItemStackContainer(ItemStack.EMPTY);
    }
    
    private ItemStackContainer createItemStackContainer(ItemStack stack) {
        ItemStackContainer container = new ItemStackContainer(stack);
        container.setDirtyCallback(this::markDirty);
        container.setPoolReturn(this::freeItemStackContainer);
        return container;
    }
    
    private void freeItemStackContainer(ItemStackContainer container) {
        if (inUseRequestContainer.contains(container)) {
            //containers here should be all empty returning the Request containing Item is task of who ever handel's the request
            inUseRequestContainer.remove(container);
            freeRequestContainer.add(container);
        }
    }
    
    private void disposeItemStackContainer(ItemStackContainer container) {
        if (inUseRequestContainer.contains(container)) {
            //containers here should be all empty returning the Request containing Item is task of who ever handel's the request
            inUseRequestContainer.remove(container);
        }
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        addAllToQueue(inUseRequestContainer);
    }
    
    private void removeAllFromQueue(Collection<ItemStackContainer> itemStackContainer) {
        MultiBlockGroup group = IMultiBlockTile.getGroup(world, pos, getGroupType());
        if (group != null) {
            MultiBlockGroupTypeInstance instance = group.getTypeInstance();
            if (instance instanceof QueueGroupType.Instance) {
                Queue<ItemStackContainer> queue = ((QueueGroupType.Instance) instance).getQueue();
                queue.removeAll(itemStackContainer);
                return;
            }
        }
        resync = true;
    }
    
    private void addToQueue(ItemStackContainer itemStackContainer) {
        MultiBlockGroup group = IMultiBlockTile.getGroup(world, pos, getGroupType());
        if (group != null) {
            MultiBlockGroupTypeInstance instance = group.getTypeInstance();
            if (instance instanceof QueueGroupType.Instance) {
                Queue<ItemStackContainer> queue = ((QueueGroupType.Instance) instance).getQueue();
                queue.add(itemStackContainer);
                return;
            }
        }
        resync = true;
    }
    
    private void addAllToQueue(Collection<ItemStackContainer> itemStackContainerCollection) {
        MultiBlockGroup group = IMultiBlockTile.getGroup(world, pos, getGroupType());
        if (group != null) {
            MultiBlockGroupTypeInstance instance = group.getTypeInstance();
            if (instance instanceof QueueGroupType.Instance) {
                Queue<ItemStackContainer> queue = ((QueueGroupType.Instance) instance).getQueue();
                queue.addAll(itemStackContainerCollection);
            }
        }
        
        resync = true;
    }
    
    private boolean canStackBeAddedToQueue(ItemStack stack) {
        //do we have room, is there even some thing to insert and does crafting exists
        if (freeRequestContainer.isEmpty() || stack.isEmpty() || capabilityCraftingRequest == null) {
            return false;
        }
        
        //is the ItemStack a Request and is it uncompleted
        ICraftingRequest request = stack.getCapability(capabilityCraftingRequest, null);
        
        return null != request && !request.isCompleted();
        
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return QueueGui.tryCreateInstance(id, player, world, x, y, z);
    }
    
    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return QueueContainer.tryCreateInstance(id, player, world, x, y, z);
    }
    
    @Override
    public void performDebugOnTile(EntityPlayer player) {
        super.performDebugOnTile(player);
        player.sendStatusMessage(new TextComponentString(String.format("%d out of %d Queue length in use!\n", inUseRequestContainer.size(), MMAConfiguration.queueLength)), false);
    }
    
    private class EnqueueingItemHandler implements IItemHandler {
        
        @Override
        public int getSlots() {
            return 1;
        }
        
        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }
        
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            //slot in range
            if (slot >= getSlots() || slot < 0) {
                if (!simulate) {
                    //Are you a QA tester?
                    throw new IndexOutOfBoundsException(
                            String.format("Attempted Insert into Slot:%d for Size:%d!", slot, getSlots()));
                } else {
                    //they may not try to really insert if they get the whole stack back
                    return stack;
                }
            }
            
            //TODO check if other queues in the MultiBlock have ISC's left and use those if we are out
            if (!canStackBeAddedToQueue(stack)) {
                return stack;
            }
            
            //create two copies one to return and one to enqueue
            ItemStack enqueue = stack.copy();
            ItemStack result;
            
            //set the stack we want to enqueue to size 1 while decreasing the returned stack, we don't want to dupe requests
            if (stack.getCount() != 1) {
                result = stack.copy();
                result.shrink(1);
                enqueue.setCount(1);
            } else {
                result = ItemStack.EMPTY;
            }
            
            if (simulate)
                return result;
            
            ItemStackContainer container = freeRequestContainer.poll();
            container.setItemStack(enqueue);
            inUseRequestContainer.add(container);
            addToQueue(container);
            
            return result;
        }
        
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            //we don't allow extraction
            return ItemStack.EMPTY;
        }
        
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    }
}
