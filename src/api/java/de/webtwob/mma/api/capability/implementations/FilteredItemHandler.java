package de.webtwob.mma.api.capability.implementations;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Created by bennet on 27.03.17.
 * This is an ItemStackHandler that can be filtered you can either pass
 * a NonNullList or an ItemStackHandler which will be used to store the Items
 */
public class FilteredItemHandler extends ItemHandlerWrapper {

    private final Predicate<ItemStack> filter;
    private final int                  limit;
    protected     ItemStackHandler     handler;

    /**
     * @param stack        a NonNullList of ItemStacks for storing the Items will be wrapped in an ItemStackHandler
     * @param filter       a filter that returns true for all accepted Items
     * @param maxStackSize the max stack size for a single Stack, the Sack size of Items still applies
     */
    public FilteredItemHandler(NonNullList<ItemStack> stack, Predicate<ItemStack> filter, int maxStackSize) {
        super(stack);
        limit = maxStackSize;
        this.filter = filter;
        handler = new ItemStackHandler(stack);
    }

    /**
     * @param stackHandler an ItemStackHandler for storing the Items
     * @param filter       a filter that returns true for all accepted Items
     * @param maxStackSize the max stack size for a single Stack, the Sack size of Items still applies
     */
    public FilteredItemHandler(ItemStackHandler stackHandler, Predicate<ItemStack> filter, int maxStackSize) {
        super();
        limit = maxStackSize;
        this.filter = filter;
        handler = stackHandler;
    }

    @Override
    public void setSize(int size) {
        //can't resize because if we get a NonNullList in the constructor we need to keep using it
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        /*
         * before letting the Wrapped ItemHandler decide if the item fits we test if it matches our filter
         * */
        if (!filter.test(stack)) {
            return stack;
        }
        return handler.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return handler.extractItem(slot, amount, simulate);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        handler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlotLimit(int slot) {
        /*
         * applying the maxStack limit by taking the lower of the Items limit and our limit
         * */
        return Math.min(limit, handler.getSlotLimit(slot));
    }

}
