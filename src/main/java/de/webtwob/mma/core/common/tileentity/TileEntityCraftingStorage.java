package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.crafting.ItemStackContainer;
import de.webtwob.mma.api.registries.MultiBlockGroupType;
import de.webtwob.mma.core.common.config.MMAConfiguration;
import de.webtwob.mma.core.common.references.NBTKeys;
import de.webtwob.mma.core.common.references.ObjectHolders;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityCraftingStorage extends MultiBlockTileEntity {

    /**
     * Containers that contain Items
     * */
    private final List<ItemStackContainer> usedContainers     = new LinkedList<>();
    /**
     * Containers that contain Items and are not used by a machine //TODO return them to storage
     * */
    private final List<ItemStackContainer> availableContainer = new LinkedList<>();
    /**
     * Containers that are ready to be used
     * */
    private final List<ItemStackContainer> unusedContainer    = new LinkedList<>();

    private final List<ItemStack> storageIndexerLinks = new LinkedList<>();

    /**
     * Creates a new TileEntityCraftingStorage with a fixed Size unmodifiable {@literal List<ItemStackContainer>}
     * with size based on the value found in the configuration file
     */
    public TileEntityCraftingStorage() {
        ItemStackContainer[] container = new ItemStackContainer[MMAConfiguration.storageStackLimit];
        Arrays.setAll(container, i -> createNewItemStackContainer(ItemStack.EMPTY));
        unusedContainer.addAll(Arrays.asList(container));
    }

    private ItemStackContainer createNewItemStackContainer(ItemStack stack) {
        ItemStackContainer container = new ItemStackContainer(stack);
        container.setDirtyCallback(this::markDirty);
        container.setPoolReturn(this::returnISCToPool);
        return container;
    }

    private void returnISCToPool(ItemStackContainer container) {
        usedContainers.add(container);
        unusedContainer.remove(container);
    }

    @Override
    public MultiBlockGroupType getGroupType() {
        return ObjectHolders.MANAGER_CRAFTING;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbtc) {
        NBTTagCompound compound = super.writeToNBT(nbtc);
        NBTTagList     itemList = new NBTTagList();
        for (ItemStackContainer isc : usedContainers) {
            ItemStack stack = isc.getItemStack();
            if (!stack.isEmpty()) {
                itemList.appendTag(stack.serializeNBT());
            }
        }
        compound.setTag(NBTKeys.CRAFTING_STORAGE_ITEM_LIST, itemList);

        NBTTagList links = new NBTTagList();
        for (ItemStack stack : storageIndexerLinks) {
            links.appendTag(stack.serializeNBT());
        }

        compound.setTag(NBTKeys.CRAFTING_STORAGE_LINK_LIST, links);
        return compound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList itemList = compound.getTagList(NBTKeys.CRAFTING_STORAGE_ITEM_LIST, Constants.NBT.TAG_COMPOUND);
        int        sizeNBT  = itemList.tagCount();
        usedContainers.clear();

        for (int i = 0; i < sizeNBT; i++) {
            ItemStackContainer container = createNewItemStackContainer(new ItemStack(itemList.getCompoundTagAt(i)));
            if (i >= MMAConfiguration.storageStackLimit) {
                container.setPoolReturn(usedContainers::remove);
            }
            usedContainers.add(createNewItemStackContainer(new ItemStack(itemList.getCompoundTagAt(i))));
            availableContainer.addAll(usedContainers);
        }

        int rest = MMAConfiguration.storageStackLimit - usedContainers.size();
        if (rest <= 0) {
            unusedContainer.clear();
            return;
        }
        while (unusedContainer.size() > rest) {
            unusedContainer.remove(0);
        }
        while (unusedContainer.size() < rest) {
            unusedContainer.add(createNewItemStackContainer(ItemStack.EMPTY));
        }

        storageIndexerLinks.clear();
        NBTTagList links = compound.getTagList(NBTKeys.CRAFTING_STORAGE_LINK_LIST, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < links.tagCount(); i++) {
            storageIndexerLinks.add(new ItemStack((NBTTagCompound) links.get(i)));
        }
    }

    public ItemStackContainer getISC() {
        if(unusedContainer.isEmpty()){
            return null;
        }
        ItemStackContainer container = unusedContainer.remove(0);
        usedContainers.add(container);
        return container;
    }

    public Stream<ItemStack> getLinks(){
        return storageIndexerLinks.stream();
    }
}
