package de.webtwob.mbma.api.capability.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Created by bennet on 27.03.17.
 */
public class FilteredItemHandler extends ItemStackHandler {

    private final Predicate<ItemStack> filter;
    private final int limit;

    public FilteredItemHandler(NonNullList<ItemStack> stack, Predicate<ItemStack> filter, int maxStackSize) {
        super(stack);
        limit=maxStackSize;
        this.filter = filter;
    }

    @Override
    public void setSize(int size) {
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!filter.test(stack)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return limit;
    }


}
