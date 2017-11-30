package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.crafting.ItemStackContainer;
import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mma.api.interfaces.tileentity.IItemMoveRequest;
import de.webtwob.mma.api.property.MMAProperties;
import de.webtwob.mma.api.registries.MultiBlockGroupType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityStorageIndexer extends MultiBlockTileEntity {
    
    @ObjectHolder("mmacore:storage")
    private static final MultiBlockGroupType MANAGER_STORAGE = null;
    private static Capability<IItemHandler> capabilityItemHandler = null;
    private LinkedList<IItemMoveRequest> requests = new LinkedList<>();
    private NonNullList<ItemStack> storageLinks = NonNullList.create();
    
    @CapabilityInject(IItemHandler.class)
    private static void setCapabilityItemHandler(Capability<IItemHandler> itemHandlerCapability) {
        capabilityItemHandler = itemHandlerCapability;
    }
    
    @Override
    public MultiBlockGroupType getGroupType() {
        return MANAGER_STORAGE;
    }
    
    @Override
    public void update() {
        super.update();
        
        if (!requests.isEmpty()) {
            IItemMoveRequest request = requests.pollFirst();
            switch (request.getType()) {
                
                case REQUEST_ITEMS:
                    handleRequest(request);
                    break;
                case DEPOSIT_ITEMS:
                    handleDeposit(request);
                    break;
                default:
                    //for future cases we don't do anything yet
            }
            request.passOnRequest();
        }
        
    }
    
    private void handleDeposit(final IItemMoveRequest request) {
        ItemStackContainer container = request.getItemContainer();
        for (IItemHandler handler : getInventories()) {
            container.setItemStack(ItemHandlerHelper.insertItem(handler, container.getItemStack(), false));
            if (container.getItemStack().isEmpty()) {
                return;
            }
        }
    }
    
    private void handleRequest(final IItemMoveRequest request) {
        //TODO simplify
        ItemStackContainer container = request.getItemContainer();
        for (IItemHandler handler : getInventories()) {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stackInSlot = handler.getStackInSlot(i);
                ItemStack inContainer = container.getItemStack();
                if (stackInSlot.isItemEqual(request.getRequest()) && (inContainer.isEmpty() || ItemHandlerHelper.canItemStacksStack(stackInSlot, inContainer))) {
                    if (!inContainer.isEmpty()) {
                        int spaceLeft = inContainer.getMaxStackSize() - inContainer.getCount();
                        inContainer.grow(handler.extractItem(i, spaceLeft, false).getCount());
                    } else {
                        container.setItemStack(inContainer = handler.extractItem(i, request.getRequest().getMaxStackSize(), false));
                    }
                }
                if (inContainer.getCount() >= inContainer.getMaxStackSize()) {
                    return;
                }
            }
        }
    }
    
    private List<IItemHandler> getInventories() {
        List<IItemHandler> handlerList = new ArrayList<>();
        if (capabilityItemHandler == null) {
            return handlerList;
        }
        return storageLinks.stream()
                .map(IBlockPosProvider::getBlockPos)
                .filter(Objects::nonNull)
                .map(
                        p -> new Object() {
                            final BlockPos pos = p;
                            final IBlockState state = world.getBlockState(pos);
                        }
                )
                .filter(o -> o.state.getPropertyKeys().contains(MMAProperties.CONNECTED))
                .filter(o -> o.state.getValue(MMAProperties.CONNECTED))
                .filter(o -> o.state.getPropertyKeys().contains(MMAProperties.FACING))
                .map(
                        o -> new Object() {
                            final EnumFacing facing = o.state.getValue(MMAProperties.FACING);
                            final TileEntity tileEntity = world.getTileEntity(o.pos.offset(facing));
                        }
                )
                .map(o -> o.tileEntity != null ? o.tileEntity.getCapability(capabilityItemHandler, o.facing.getOpposite()) : null)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    /**
     * Adds a IItemMoveRequest to the queue of requests to be handled
     * Requests are handled on this TileEntity update Method being called  usually every Tick
     *
     * @param request the request to add
     */
    public void addItemMoveRequest(IItemMoveRequest request) {
        requests.add(request);
    }
    
}
