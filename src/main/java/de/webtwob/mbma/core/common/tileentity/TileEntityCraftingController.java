package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.enums.MachineState;
import de.webtwob.mbma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequestProvider;
import de.webtwob.mbma.api.registries.MultiBlockGroupType;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityCraftingController extends MultiBlockTileEntity {
    
    @ObjectHolder("mbmacore:crafting")
    public static final MultiBlockGroupType MANAGER_CRAFTING = null;
    
    @CapabilityInject(ICraftingRequestProvider.class)
    private static Capability<ICraftingRequestProvider> REQUEST_PROVIDER = null;
    @CapabilityInject(ICraftingRequest.class)
    private static Capability<ICraftingRequest> CAPABILITY_REQUEST = null;
    
    private static int WAIT_TIME = 20;
    private final List<Function<TileEntityCraftingController, String>> ERROR_SOLVED = new LinkedList<>();
    private final List<Function<TileEntityCraftingController, String>> WAIT_CONDITION = new LinkedList<>();
    private final List<String> errors = new LinkedList<>();
    private final List<String> waiting = new LinkedList<>();
    @Nonnull
    private MachineState state = MachineState.IDLE;
    private int pause = WAIT_TIME;
    private NonNullList<ItemStack> queueLinkCards = NonNullList.create();
    private NonNullList<ItemStack> patternLinkCards = NonNullList.create();
    @Nonnull
    private ItemStack currentRequest = ItemStack.EMPTY;
    
    @Nonnull
    public MachineState getState() {
        return state;
    }
    
    private void setState(MachineState state) {
        this.state = state;
        markDirty();
    }
    
    @Override
    public MultiBlockGroupType getGroupType() {
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
                    if (currentRequest.isEmpty()) {
                        //we don't have a task therefor we stop working
                        setState(MachineState.IDLE);
                        markDirty();
                        break;
                    }
                    //TODO
                    //continue active planning
                    break;
                case WAITING:
                    waitOrError(waiting, WAIT_CONDITION);
                    break;
                case PROBLEM:
                    waitOrError(errors, ERROR_SOLVED);
                    break;
            }
        } else {
            pause--;
        }
    }
    
    private void waitOrError(final List<String> descriptions, final List<Function<TileEntityCraftingController, String>> functions) {
        descriptions.clear();
        functions.stream()
                .map(e -> e.apply(this))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> descriptions));
        if (descriptions.isEmpty()) {
            functions.clear();
            setState(MachineState.IDLE);
        } else {
            pause = WAIT_TIME;
        }
    }
    
    /**
     * What to do in update when in the IDLE state
     */
    private void idle() {
        if (currentRequest.isEmpty()) {
            
            //do we have at least one linked queue
            if (!getRequestProviders().findAny().isPresent()) {
                ERROR_SOLVED.add(t -> !t.getRequestProviders().findAny().isPresent() ? "mbmacor:error.desc.noqueues" : null);
                setState(MachineState.PROBLEM);
                return;
            }
            
            //find and get first request
            if (getNewRequest()) {
                setState(MachineState.RUNNING);
                return;
            }
            pause = WAIT_TIME;
        } else {
            setState(MachineState.RUNNING);
        }
    }
    
    private boolean canHandleRequest(ItemStack stack) {
        ICraftingRequest request;
        if ((request = stack.getCapability(CAPABILITY_REQUEST, null)) != null && !request.isCompleted()) {
        /*
        TODO:
            return true if a PatternStore contains a Pattern resulting in the requests requested item
        * */
        }
        return false;
    }
    
    private Stream<ICraftingRequestProvider> getRequestProviders() {
        return queueLinkCards.stream()
                .map(IBlockPosProvider::getBlockPos)
                .filter(Objects::nonNull)
                .map(world::getTileEntity)
                .filter(Objects::nonNull)
                .map(te -> te.getCapability(REQUEST_PROVIDER, null))
                .filter(Objects::nonNull);
    }
    
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    private boolean getNewRequest() {
        return !(currentRequest = getRequestProviders()
                .map(cap -> cap.getRequestIfRequirementHolds(this::canHandleRequest))
                .filter(Objects::nonNull)
                .findFirst().orElse(ItemStack.EMPTY)).isEmpty();
    }
    
}
