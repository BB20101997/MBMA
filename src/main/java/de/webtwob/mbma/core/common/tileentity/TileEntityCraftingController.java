package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.enums.MachineState;
import de.webtwob.mbma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.api.multiblock.MultiBlockGroupManager;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityCraftingController extends MultiBlockTileEntity {

    @ObjectHolder("mbmacore:crafting")
    public static final MultiBlockGroupManager MANAGER_CRAFTING = null;

    private static int WAIT_TIME = 20;
    private static int MAX_QUEUES = 5;
    private static int MAX_PATTERN_STORES = 5;

    @Nonnull
    private MachineState state = MachineState.IDLE;

    private int pause = WAIT_TIME;

    private final List<Function<TileEntityCraftingController, String>> ERROR_SOLVED = new LinkedList<>();
    private final List<Function<TileEntityCraftingController,String>> WAIT_CONDITION = new LinkedList<>();
    
    private final List<String> errors = new LinkedList<>();
    private final List<String> waiting = new LinkedList<>();

    private NonNullList<ItemStack> queueLinkCards = NonNullList.create();
    private NonNullList<ItemStack> patternLinkCards = NonNullList.create();

    @Nonnull
    private ItemStack currentRequest = ItemStack.EMPTY;

    @Override
    public MultiBlockGroupManager getManager() {
        return MANAGER_CRAFTING;
    }

    @Override
    public void update() {
        super.update();
        if (pause <= 0) {
            switch (state) {
                case IDLE:
                    idle();
                    break;
                case RUNNING:
                    if(currentRequest.isEmpty()){
                        //we don't have a task therefor we stop working
                        state = MachineState.IDLE;
                        break;
                    }

                    //continue active planning
                    break;
                case WAITING:
                    waitOrError(waiting,WAIT_CONDITION);
                    break;
                case PROBLEM:
                    waitOrError(errors,ERROR_SOLVED);
                    break;
            }
        } else {
            pause--;
        }
    }


    private void waitOrError(final List<String> descriptions, final List<Function<TileEntityCraftingController,String>> functions){
        descriptions.clear();
        functions.stream()
                .map(e->e.apply(this))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(()->descriptions));
        if(descriptions.isEmpty()){
            functions.clear();
            state =  MachineState.IDLE;
        }else{
            pause = WAIT_TIME;
        }
    }

    /**
     * What to do in update when in the IDLE state
     * */
    private void idle() {
        if (currentRequest.isEmpty()) {
            List<IItemHandler> handlerList = getLinkedQueues();

            if(handlerList.isEmpty()){
                ERROR_SOLVED.add((t)->t.getLinkedQueues().isEmpty()?"mbmacor:error.desc.noqueues":null);
                state = MachineState.PROBLEM;
                return;
            }

            //find and get first request
            for (IItemHandler handler : handlerList) {
                ItemStack stack;
                for (int i = 0; i < handler.getSlots(); i++) {
                    stack = handler.getStackInSlot(i);
                    ICraftingRequest request;
                    if (!stack.isEmpty() && (request=stack.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null))!=null) {
                        if(canHandleRequest(request)){
                            currentRequest = handler.extractItem(i, 1, false);
                            if (!currentRequest.isEmpty()) {
                                state = MachineState.RUNNING;
                                return;
                            }
                        }
                    }
                }
            }
            pause = WAIT_TIME;
        }else{
            state = MachineState.RUNNING;
        }
    }

    private boolean canHandleRequest(ICraftingRequest request) {
        return false;
    }

    private List<IItemHandler> getLinkedQueues() {
        return queueLinkCards.stream()
                .map(IBlockPosProvider::getBlockPos)
                .filter(Objects::nonNull)
                .map(world::getTileEntity)
                .filter(Objects::nonNull)
                .map(te -> te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
