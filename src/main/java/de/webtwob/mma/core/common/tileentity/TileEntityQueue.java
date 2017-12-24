package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.crafting.ItemStackContainer;
import de.webtwob.mma.api.interfaces.capability.ICraftingRequestProvider;
import de.webtwob.mma.api.interfaces.gui.IGUIHandlerBoth;
import de.webtwob.mma.api.interfaces.tileentity.IMultiBlockTile;
import de.webtwob.mma.api.multiblock.MultiBlockGroup;
import de.webtwob.mma.api.multiblock.MultiBlockGroupTypeInstance;
import de.webtwob.mma.api.multiblock.MultiBlockMember;
import de.webtwob.mma.api.registries.MultiBlockGroupType;
import de.webtwob.mma.api.util.MMAFilter;
import de.webtwob.mma.core.client.gui.QueueGui;
import de.webtwob.mma.core.common.config.MMAConfiguration;
import de.webtwob.mma.core.common.inventory.QueueContainer;
import de.webtwob.mma.core.common.multiblockgroups.QueueGroupType;
import de.webtwob.mma.core.common.references.CapabilityInjections;
import de.webtwob.mma.core.common.references.LogMessages;
import de.webtwob.mma.core.common.references.NBTKeys;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
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

    private final LinkedList<ItemStackContainer> freeRequestContainer  = new LinkedList<>();
    private final LinkedList<ItemStackContainer> inUseRequestContainer = new LinkedList<>();

    private IItemHandler handler = new EnqueueingItemHandler();
    private boolean      resync  = false;

    public TileEntityQueue() {
        //init freeItemStackContainers
        for (int i = 0; i < MMAConfiguration.queueLength; i++) {
            freeRequestContainer.add(createItemStackContainer());
        }
    }

    public MultiBlockGroupType getGroupType() {
        return MANAGER_QUEUE;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return super.hasCapability(capability, facing) || null != getCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        Capability<IItemHandler>             itemHandlerCapability             = CapabilityInjections.getCapabilityItemHandler();
        Capability<ICraftingRequestProvider> craftingRequestProviderCapability = CapabilityInjections.getCapabilityRequestProvider();

        if (null != itemHandlerCapability && itemHandlerCapability == capability) {
            return (T) handler;
        } else if (null != craftingRequestProviderCapability && craftingRequestProviderCapability == capability) {
            return (T) (ICraftingRequestProvider) this::groupGetRequestIfRequirementHolds;
        }
        return super.getCapability(capability, facing);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound supComp = super.writeToNBT(compound);
        NBTTagList     items   = new NBTTagList();
        inUseRequestContainer.forEach(stack -> items.appendTag(stack.getItemStack().serializeNBT()));
        supComp.setTag(NBTKeys.QUEUE_STACKS, items);
        return supComp;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        removeAllFromQueue(inUseRequestContainer);

        inUseRequestContainer.clear();
        int                free = MMAConfiguration.queueLength;
        ItemStackContainer container;
        for (NBTBase comp : compound.getTagList(NBTKeys.QUEUE_STACKS, Constants.NBT.TAG_COMPOUND)) {
            container = createItemStackContainer(new ItemStack((NBTTagCompound) comp));
            inUseRequestContainer.add(container);
            free--;
            if (0 >= free) {
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
    public Queue<ItemStackContainer> getCurrentRequests() {
        MultiBlockGroup group = IMultiBlockTile.getGroup(world, pos, getGroupType());
        if (null != group) {
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
        if (null == group) {
            return ItemStack.EMPTY;
        }

        Queue<ItemStackContainer> queue = getCurrentRequests();

        return queue.stream().filter(isc -> requirement.test(isc.getItemStack())).findFirst().map(isc -> {
            ItemStack request = isc.getItemStack();
            queue.remove(isc);
            isc.setItemStack(ItemStack.EMPTY);
            isc.returnToPool();
            return request;
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    public void update() {
        super.update();
        if (resync) {
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
        if (null != group) {
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
        if (null != group) {
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
        if (null != group) {
            MultiBlockGroupTypeInstance instance = group.getTypeInstance();
            if (instance instanceof QueueGroupType.Instance) {
                Queue<ItemStackContainer> queue = ((QueueGroupType.Instance) instance).getQueue();
                queue.addAll(itemStackContainerCollection);
            }
        }

        resync = true;
    }

    public boolean canStackBeAddedToQueue(ItemStack stack) {

        if (!MMAFilter.MUSTER_FILTER.test(stack) || stack.isEmpty()) {
            return false;
        }

        MultiBlockGroup group = IMultiBlockTile.getGroup(world, pos, getGroupType());

        return group != null && group.getMembers()
                                     .stream()
                                     .map(MultiBlockMember::getPos)
                                     .filter(world::isBlockLoaded)
                                     .map(world::getTileEntity)
                                     .filter(TileEntityQueue.class::isInstance)//TODO replace hardcoded TEQ
                                     .map(TileEntityQueue.class::cast)
                                     .anyMatch(q -> !q.freeRequestContainer.isEmpty());
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return QueueGui.tryCreateInstance(player, world, x, y, z);
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return QueueContainer.tryCreateInstance(player, world, x, y, z);
    }

    @Override
    public void performDebugOnTile(EntityPlayer player) {
        super.performDebugOnTile(player);
        player.sendStatusMessage(
                new TextComponentString(String.format(LogMessages.TE_Q_DEBUG, inUseRequestContainer.size(),
                                                      MMAConfiguration.queueLength
                )), false);
    }

    public void addStackToQueue(final ItemStack stack) {
        MultiBlockGroup group = getGroup();
        TileEntityQueue queue = group.getMembers()
                                     .stream()
                                     .map(MultiBlockMember::getPos)
                                     .filter(world::isBlockLoaded)
                                     .map(world::getTileEntity)
                                     .filter(TileEntityQueue.class::isInstance)//TODO replace hardcoded TEQ
                                     .map(TileEntityQueue.class::cast)
                                     .filter(q -> !q.freeRequestContainer.isEmpty())
                                     .findFirst()
                                     .orElse(null);

        if (null == queue) {
            throw new IllegalStateException(
                    "No free ISC! You need to make sure to test if an ItemStack can be added first befor adding it!");
        }

        ItemStackContainer isc = queue.freeRequestContainer.poll();
        isc.setItemStack(stack);
        queue.inUseRequestContainer.add(isc);
        addToQueue(isc);
    }

    @Override
    public void onBlockBreak(final World world, final BlockPos pos) {
        super.onBlockBreak(world, pos);
        removeAllFromQueue(inUseRequestContainer);
        for (ItemStackContainer isc : inUseRequestContainer) {
            ItemStack stack = isc.getItemStack();
            if (!stack.isEmpty()) {
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
            }
        }
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
            if (slot >= getSlots() || 0 > slot) {
                if (!simulate) {
                    //Are you a QA tester?
                    throw new IndexOutOfBoundsException(
                            String.format("Attempted Insert into Slot:%d for Size:%d!", slot, getSlots()));
                } else {
                    //they may not try to really insert if they get the whole stack back
                    return stack;
                }
            }

            if (!canStackBeAddedToQueue(stack)) {
                return stack;
            }

            //create two copies one to return and one to enqueue
            ItemStack enqueue = stack.copy();
            ItemStack result;

            //set the stack we want to enqueue to size 1 while decreasing the returned stack, we don't want to dupe requests
            if (1 != stack.getCount()) {
                result = stack.copy();
                result.shrink(1);
                enqueue.setCount(1);
            } else {
                result = ItemStack.EMPTY;
            }

            if (simulate) {
                return result;
            }

            addStackToQueue(stack);

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
