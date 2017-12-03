package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.api.interfaces.capability.ICraftingRequestProvider;
import de.webtwob.mma.api.multiblock.MultiBlockMember;
import de.webtwob.mma.api.registries.MultiBlockGroupType;
import de.webtwob.mma.core.common.config.MMAConfiguration;
import de.webtwob.mma.core.common.references.NBTKeys;

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

    @ObjectHolder("mmacore:queue")
    private static final MultiBlockGroupType MANAGER_QUEUE = null;

    private static Capability<IItemHandler> capabilityItemHandler;
    private static Capability<ICraftingRequest> capabilityCraftingRequest;
    private static Capability<ICraftingRequestProvider> capabilityCraftingRequestProvider;

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
        requestList.forEach(stack -> items.appendTag(stack.serializeNBT()));
        supComp.setTag(NBTKeys.QUEUE_STACKS, items);
        return supComp;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        requestList.clear();
        compound.getTagList(NBTKeys.QUEUE_STACKS, Constants.NBT.TAG_COMPOUND).forEach(comp -> requestList.add(new ItemStack((NBTTagCompound) comp)));
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
                .map(stack -> stack.getCapability(capabilityCraftingRequest, null))
                .filter(Objects::nonNull).toArray(ICraftingRequest[]::new);
    }

    /**
     * find the first Group Member that returns a non empty ItemStack and return said ItemStack
     */
    @Nonnull
    private ItemStack groupGetRequestIfRequirementHolds(@Nonnull final Predicate<ItemStack> requirement) {
        if(group==null){
            return ItemStack.EMPTY;
        }
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
            if (requestList.size() >= MMAConfiguration.queueLength || stack.getCount() <= 0) {
                return stack;
            }


            ICraftingRequest request;

            //is the ItemStack a Request and is it uncompleted
            if (capabilityCraftingRequest !=null &&((request = stack.getCapability(capabilityCraftingRequest, null)) == null || request.isCompleted())) {
                return stack;
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

    @CapabilityInject(IItemHandler.class)
    private static void injectItemHandler(Capability<IItemHandler> handler){
        capabilityItemHandler = handler;
    }

    @CapabilityInject(ICraftingRequest.class)
    private static void injectCraftingRequest(Capability<ICraftingRequest> handler){
        capabilityCraftingRequest = handler;
    }

    @CapabilityInject(ICraftingRequestProvider.class)
    private static void injectCraftingRequestProvider(Capability<ICraftingRequestProvider> handler){
        capabilityCraftingRequestProvider = handler;
    }
}
