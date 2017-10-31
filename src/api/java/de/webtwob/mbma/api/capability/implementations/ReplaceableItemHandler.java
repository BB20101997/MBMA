package de.webtwob.mbma.api.capability.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Predicate;

/**
 * Created by bennet on 01.04.17.
 */
public class ReplaceableItemHandler extends FilteredItemHandler {

    public ReplaceableItemHandler(NonNullList<ItemStack> list, Predicate<ItemStack> filter, int maxSize) {
        super(list, filter, maxSize);
    }

    public void replaceItemList(NonNullList<ItemStack> list) {
        stacks = list;
        handler = new ItemStackHandler(list);
    }

}
