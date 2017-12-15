package de.webtwob.mma.api.capability.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Predicate;

/**
 * Created by bennet on 01.04.17.
 */
public class ReplaceableItemHandler extends FilteredItemHandler {

    /**
     * This extension of the FilteredItemHandler makes it possible to replace NonNullList that is used to store the Items
     * making it possible to replace all contained Items at once
     *
     * @param list    the NonNullList used at to use on creation
     * @param filter  the filter to filter inserted Items by
     * @param maxSize the max Stack size for all ItemStacks
     */
    public ReplaceableItemHandler(NonNullList<ItemStack> list, Predicate<ItemStack> filter, int maxSize) {
        super(list, filter, maxSize);
    }

    /**
     * @param list the list that shall replace the current one3
     */
    public void replaceItemList(NonNullList<ItemStack> list) {
        stacks = list;
        handler = new ItemStackHandler(list);
    }

}
