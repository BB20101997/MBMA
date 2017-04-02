package de.webtwob.mbma.api.capability.implementations;

import de.webtwob.mbma.api.interfaces.IObjectCondition;
import de.webtwob.mbma.common.item.MBMAItemList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * Created by bennet on 27.03.17.
 */
public class FilteredItemHandler extends ItemStackHandler {

    private static final ItemStack ITEM_STACK = new ItemStack(MBMAItemList.LINKCARD, 0);

    private final IObjectCondition<ItemStack> filter;
    private final int limit;

    public FilteredItemHandler(NonNullList<ItemStack> stack, IObjectCondition<ItemStack> filter, int maxStackSize) {
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
        if (!filter.checkCondition(stack)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return limit;
    }


}
