package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.capability.implementations.DefaultCraftingRequest;
import de.webtwob.mma.api.enums.MachineState;
import de.webtwob.mma.api.interfaces.capability.*;
import de.webtwob.mma.api.interfaces.gui.IGUIHandlerBoth;
import de.webtwob.mma.api.interfaces.tileentity.IMachine;
import de.webtwob.mma.api.registries.MultiBlockGroupType;
import de.webtwob.mma.core.client.gui.CraftingControllerGui;
import de.webtwob.mma.core.common.config.MMAConfiguration;
import de.webtwob.mma.core.common.inventory.CraftingControllerContainer;
import de.webtwob.mma.core.common.multiblockgroups.CraftingGroupType;
import de.webtwob.mma.core.common.references.CapabilityInjections;
import de.webtwob.mma.core.common.references.NBTKeys;
import de.webtwob.mma.core.common.references.ObjectHolders;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityCraftingController extends MultiBlockTileEntity
        implements IGUIHandlerBoth, IMachine<TileEntityCraftingController> {

    private static final int                                                  WAIT_TIME      = 20;
    private final        List<Function<TileEntityCraftingController, String>> errors         = new LinkedList<>();
    private final        List<Function<TileEntityCraftingController, String>> waitConditions = new LinkedList<>();
    private final        List<String>                                         errorMessages  = new LinkedList<>();
    private final        List<String>                                         waitMessages   = new LinkedList<>();

    @Nonnull
    private MachineState           state            = MachineState.IDLE;
    private int                    pause            = WAIT_TIME;
    private NonNullList<ItemStack> queueLinkCards   = NonNullList.create();
    private NonNullList<ItemStack> patternLinkCards = NonNullList.create();

    private Queue<ICraftingRequest> craftQueue = new LinkedList<>();

    @Nonnull
    private ItemStack currentRequest = ItemStack.EMPTY;

    private static String missingRecipe(TileEntityCraftingController tecc) {
        ICraftingRequest request = ICraftingRequest.getCraftingRequest(tecc.currentRequest);
        if (request == null || request.isCompleted()) {
            tecc.setState(MachineState.IDLE);
            tecc.markDirty();
            return null;
        }

        ICraftingRecipe provider = tecc.getPatternForRequest(request);

        if (provider == null) {
            return "mmacore.error.desc.no_recipe";
        }

        return null;
    }

    private static String missingQueue(TileEntityCraftingController tecc) {
        if (!tecc.getRequestProviders().findAny().isPresent()) {
            return "mmacore.error.desc.no_queues";
        } else {
            return null;
        }
    }

    @Nonnull
    public MachineState getState() {
        return state;
    }

    public void setState(@Nonnull MachineState state) {
        this.state = state;
        markDirty();
    }

    @Override
    public int getDefaultWaitTime() {
        return MMAConfiguration.defaultWaitTicks;
    }

    @Override
    public int getWaitTicks() {
        return pause;
    }

    @Override
    public void setWaitTicks(final int tick) {
        pause = tick;
    }

    @Override
    public MultiBlockGroupType getGroupType() {
        return ObjectHolders.MANAGER_CRAFTING;
    }

    @Override
    public void update() {
        super.update();
        if (0 >= pause) {
            switch (state) {
                case IDLE:
                    idle();
                    break;
                case RUNNING:
                    run();
                    break;
                case WAITING:
                    waitOrError(waitMessages, waitConditions);
                    break;
                case PROBLEM:
                    waitOrError(errorMessages, errors);
                    break;
            }
        } else {
            pause--;
        }
    }

    private void run() {
        if (craftQueue.isEmpty()) {
            if (currentRequest.isEmpty()) {
                //we don't have a task therefor we stop working
                setState(MachineState.IDLE);
                markDirty();
                return;
            }

            ICraftingRequest request = ICraftingRequest.getCraftingRequest(currentRequest);
            if (request == null || request.isCompleted()) {
                //task is done back to IDLE
                setState(MachineState.IDLE);
                markDirty();
                return;
            }

            ICraftingRecipe provider = getPatternForRequest(request);

            if (provider == null) {
                //No Recipe found this is a Problem
                errors.add(TileEntityCraftingController::missingRecipe);
                setState(MachineState.PROBLEM);
                return;
            }

            for (ItemStack stack : provider.getInputs()) {
                ICraftingRequest request1 = new DefaultCraftingRequest();
                request1.setQuantity(stack.getCount());
                request1.setRequest(stack);
                craftQueue.add(request1);

            }
        } else {
            //TODO work on queue
        }
    }

    private ICraftingRecipe getPatternForRequest(final ICraftingRequest request) {
        return CraftingGroupType.getPatternForRequest(request, getGroup(), world);
    }

    /**
     * What to do in update when in the IDLE state
     */
    private void idle() {
        if (currentRequest.isEmpty()) {

            //do we have at least one linked queue
            if (!getRequestProviders().findAny().isPresent()) {
                errors.add(TileEntityCraftingController::missingQueue);
                setState(MachineState.PROBLEM);
                return;
            }

            //find and get first request
            if (tryToGetNewRequest()) {
                setState(MachineState.RUNNING);
                return;
            }
            pause = WAIT_TIME;
        } else {
            setState(MachineState.RUNNING);
        }
    }

    private boolean canHandleRequest(final ItemStack stack) {
        ICraftingRequest request = ICraftingRequest.getCraftingRequest(stack);
        //noinspection SimplifiableIfStatement
        if (request.isCompleted()) {
            return false;
        }
        return (patternLinkCards.stream()
                                .map(IBlockPosProvider::getBlockPos)
                                .filter(Objects::nonNull)
                                .filter(world::isBlockLoaded) //only loaded Blocks may handle requests
                                .map(world::getTileEntity)
                                .map(IPatternProvider::getIPatternProviderForTileEntity)
                                .filter(Objects::nonNull)
                                .map(IPatternProvider::getPatternList)
                                .flatMap(List::stream)
                                .filter(Objects::nonNull)
                                .anyMatch(recipe -> doesRecipeProduceProduct(recipe, stack)));
    }

    private boolean doesRecipeProduceProduct(ICraftingRecipe recipe, @Nonnull ItemStack stack) {
        return Arrays.stream(recipe.getOutputs()).anyMatch(stack::equals);
    }

    private Stream<ICraftingRequestProvider> getRequestProviders() {
        Capability<ICraftingRequestProvider> craftingRequestProviderCapability = CapabilityInjections.getCapabilityRequestProvider();
        if (null == craftingRequestProviderCapability) {
            return Stream.empty();
        }
        return queueLinkCards.stream()
                             .map(IBlockPosProvider::getBlockPos)
                             .filter(Objects::nonNull)
                             .filter(world::isBlockLoaded)
                             .map(world::getTileEntity)
                             .filter(Objects::nonNull)
                             .map(te -> te.getCapability(craftingRequestProviderCapability, null))
                             .filter(Objects::nonNull);
    }

    private boolean tryToGetNewRequest() {
        return !getRequestProviders().map(cap -> cap.getRequestIfRequirementHolds(this::canHandleRequest))
                                     .filter(Objects::nonNull)
                                     .findFirst()
                                     .orElse(ItemStack.EMPTY)
                                     .isEmpty();
    }

    public boolean canAddLinkCard() {
        return MMAConfiguration.controllerQueueCount > queueLinkCards.size();
    }

    public void addLinkCard(ItemStack stack) {
        queueLinkCards.add(stack);
        markDirty();
    }

    public NonNullList<ItemStack> getQueueLinkCards() {
        return queueLinkCards;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tileData = getTileData();

        //write linkCards to tileData

        NBTTagList itemList = new NBTTagList();

        for (ItemStack stack : queueLinkCards) {
            if (!stack.isEmpty()) {
                itemList.appendTag(stack.serializeNBT());
            }
        }

        tileData.setTag(NBTKeys.CRAFTING_CONTROLLER_QUEUE_LINKS, itemList);

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound tileData = getTileData();

        //read linkCards from tileData
        if (tileData.hasKey(NBTKeys.CRAFTING_CONTROLLER_QUEUE_LINKS, Constants.NBT.TAG_LIST)) {
            NBTTagList itemList = tileData.getTagList(
                    NBTKeys.CRAFTING_CONTROLLER_QUEUE_LINKS, Constants.NBT.TAG_COMPOUND);
            for (NBTBase itemNBT : itemList) {
                if (itemNBT instanceof NBTTagCompound) {
                    queueLinkCards.add(new ItemStack((NBTTagCompound) itemNBT));
                }
            }

        }
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return CraftingControllerGui.tryCreateInstance(player, world.getTileEntity(new BlockPos(x, y, z)));
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return CraftingControllerContainer.tryCreateInstance(player, world.getTileEntity(new BlockPos(x, y, z)));
    }

    @Override
    public void onBlockBreak(final World world, final BlockPos pos) {
        super.onBlockBreak(world, pos);
        for (ItemStack stack : queueLinkCards) {
            if (!stack.isEmpty()) {
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
            }
        }
        queueLinkCards.clear();
    }
}
