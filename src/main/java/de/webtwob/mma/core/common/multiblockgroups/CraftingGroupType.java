package de.webtwob.mma.core.common.multiblockgroups;

import de.webtwob.mma.api.crafting.ItemStackContainer;
import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mma.api.interfaces.capability.ICraftingRecipe;
import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.api.interfaces.capability.IPatternProvider;
import de.webtwob.mma.api.interfaces.tileentity.DefaultMoveRequest;
import de.webtwob.mma.api.interfaces.tileentity.IItemMoveRequest;
import de.webtwob.mma.api.interfaces.tileentity.IMoveRequestProcessor;
import de.webtwob.mma.api.multiblock.MultiBlockGroup;
import de.webtwob.mma.api.multiblock.MultiBlockMember;
import de.webtwob.mma.api.registries.MultiBlockGroupType;
import de.webtwob.mma.core.common.tileentity.TileEntityCraftingStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CraftingGroupType extends MultiBlockGroupType {

    public static boolean tryToIssueStorageRequest(IItemMoveRequest.Type type, ItemStack stack, World world, MultiBlockGroup group, Consumer<ItemStackContainer> result) {

        List<TileEntityCraftingStorage> craftingStorageList;
        Queue<IMoveRequestProcessor>    list;
        craftingStorageList = group.getMembers()
                                   .stream()
                                   .map(MultiBlockMember::getPos)
                                   .filter(Objects::nonNull)
                                   .filter(world::isBlockLoaded)
                                   .map(world::getTileEntity)
                                   .filter(TileEntityCraftingStorage.class::isInstance)//TODO move to Capability
                                   .map(TileEntityCraftingStorage.class::cast)
                                   .collect(Collectors.toList());

        ItemStackContainer container;
        container = getISC(craftingStorageList);
        if (container == null) {
            return false;
        }

        list = craftingStorageList.stream()
                                  .flatMap(TileEntityCraftingStorage::getLinks)
                                  .map(IBlockPosProvider::getBlockPos)
                                  .filter(Objects::nonNull)
                                  .filter(world::isBlockLoaded)
                                  .map(world::getTileEntity)
                                  .map(IMoveRequestProcessor::getMoveRequestProcessor)
                                  .filter(Objects::nonNull)
                                  .collect(Collectors.toCollection(LinkedList::new));

        if (type == IItemMoveRequest.Type.REQUEST_ITEMS) {
            container.setItemStack(ItemStack.EMPTY);
            new DefaultMoveRequest(stack, container, list, result);
        } else {
            container.setItemStack(stack);
            new DefaultMoveRequest(ItemStack.EMPTY, container, list, result);
        }
        return true;

    }

    private static ItemStackContainer getISC(List<TileEntityCraftingStorage> craftingStorages) {
        return craftingStorages.stream()
                               .map(TileEntityCraftingStorage::getISC)
                               .filter(Objects::nonNull)
                               .findFirst()
                               .orElse(null);
    }

    public static ICraftingRecipe getPatternForRequest(ICraftingRequest request, MultiBlockGroup group, World world) {
        return group.getMembers()
                    .stream()
                    .map(MultiBlockMember::getPos)
                    .filter(Objects::nonNull)
                    .filter(world::isBlockLoaded)
                    .map(world::getTileEntity)
                    .map(IPatternProvider::getIPatternProviderForTileEntity)
                    .filter(Objects::nonNull)
                    .flatMap(ipp -> ipp.getPatternList().stream())
                    .filter(icr -> Arrays.stream(icr.getOutputs()).anyMatch(request.getRequest()::equals))
                    .findFirst()
                    .orElse(null);
    }

}
