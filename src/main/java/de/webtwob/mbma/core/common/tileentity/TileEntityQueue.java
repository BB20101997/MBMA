package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequestProvider;
import de.webtwob.mbma.api.multiblock.MultiBlockMember;
import de.webtwob.mbma.api.registries.MultiBlockGroupType;
import de.webtwob.mbma.core.common.config.MBMAConfiguration;
import de.webtwob.mbma.core.common.references.MBMA_NBTKeys;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Predicate;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityQueue extends MultiBlockTileEntity {
    
    @ObjectHolder("mbmacore:queue")
    private static final MultiBlockGroupType MANAGER_QUEUE = null;
    
    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> ITEM_HANDLER = null;
    
    @CapabilityInject(ICraftingRequest.class)
    private static Capability<ICraftingRequest> CRAFTING_REQUEST = null;
    
    @CapabilityInject(ICraftingRequestProvider.class)
    private static Capability<ICraftingRequestProvider> REQUEST_PROVIDER = null;
    
    private final Queue<ItemStack> requestList = new LinkedList<>();
    
    private IItemHandler handler = new EnqueueingItemHandler();
    
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
        if (ITEM_HANDLER != null && ITEM_HANDLER == capability) {
            return (T) handler;
        } else if (REQUEST_PROVIDER != null && REQUEST_PROVIDER == capability) {
            return (T) (ICraftingRequestProvider) this::groupGetRequestIfRequirementHolds;
        }
        return super.getCapability(capability, facing);
    }
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound supComp = super.writeToNBT(compound);
        NBTTagList items = new NBTTagList();
        requestList.forEach(stack -> items.appendTag(stack.serializeNBT()));
        supComp.setTag(MBMA_NBTKeys.QUEUE_STACKS, items);
        return supComp;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        requestList.clear();
        compound.getTagList(MBMA_NBTKeys.QUEUE_STACKS, Constants.NBT.TAG_COMPOUND).forEach(comp -> requestList.add(new ItemStack((NBTTagCompound) comp)));
    }
    
    /**
     * //TODO: Used by the Queues GUI to show enqueued requests
     *
     * @return an Array containing the currently enqueued Requests
     */
    public ICraftingRequest[] getCurrentRequests() {
        return group.getMembers().stream()
                .map(MultiBlockMember::getPos)
                .sorted(BlockPos::compareTo)
                .map(world::getTileEntity)
                .filter(TileEntityQueue.class::isInstance)
                .map(TileEntityQueue.class::cast)
                .map(t -> t.requestList).flatMap(Collection::stream)
                .map(stack -> stack.getCapability(CRAFTING_REQUEST, null))
                .filter(Objects::nonNull).toArray(ICraftingRequest[]::new);
    }
    
    /**
     * find the first Group Member that returns a non empty ItemStack and return said ItemStack
     */
    @Nonnull
    private ItemStack groupGetRequestIfRequirementHolds(@Nonnull final Predicate<ItemStack> requirement) {
        return group.getMembers().stream()
                .map(MultiBlockMember::getPos)
                .sorted(BlockPos::compareTo)
                .map(world::getTileEntity)
                .filter(TileEntityQueue.class::isInstance)
                .map(TileEntityQueue.class::cast)
                .map(t -> t.getRequestIfRequirementHolds(requirement))
                .filter(stack -> !stack.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
    }
    
    /**
     * @param require the condition a request must satisfy
     * @return the first element of the queue satisfying the requirement or else null
     */
    @Nonnull
    private ItemStack getRequestIfRequirementHolds(@Nonnull final Predicate<ItemStack> require) {
        return requestList.stream().filter(require).findFirst().map(itemStack -> {
            requestList.remove(itemStack);
            return itemStack;
        }).orElse(ItemStack.EMPTY);
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
            if (slot >= getSlots() || slot < 0) {
                if (!simulate) {
                    //Are you a QA tester?
                    throw new IndexOutOfBoundsException(String.format("Attempted Insert into Slot:%d for Size:%d!", slot, getSlots()));
                } else {
                    //they may not try to really insert if they get the whole stack back
                    return stack;
                }
            }
            
            //do we have room and is there even some thing to insert
            if (requestList.size() >= MBMAConfiguration.queueLenght || stack.getCount() <= 0) {
                return stack;
            }
            
            {
                ICraftingRequest request;
                
                //is the ItemStack a Request and is it uncompleted
                if ((request = stack.getCapability(CRAFTING_REQUEST, null)) == null || request.isCompleted()) {
                    return stack;
                }
            }
            
            //create two copies one to return and one to enqueue
            ItemStack enqueue = stack.copy();
            ItemStack result = stack.copy();
            
            //set the stack we want to enqueue to size 1 while decreasing the returned stack, we don't want to dupe requests
            enqueue.setCount(1);
            result.shrink(1);
            
            if (simulate || requestList.add(enqueue)) {
                //either this is simulated or we succeed with adding the request to the queue therefore we return the decremented result stack
                return result;
            }
            //we are neither a simulation nor did we succeeded in adding to the queue therefore we return the original stack
            return stack;
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
