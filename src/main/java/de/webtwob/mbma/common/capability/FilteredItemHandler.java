package de.webtwob.mbma.common.capability;

import de.webtwob.mbma.common.interfaces.IStackFilter;
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

    private final IStackFilter filter;
    private final int limit;

    public FilteredItemHandler(NonNullList<ItemStack> stack, IStackFilter filter, int maxStackSize) {
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
        if (!filter.accept(stack)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return limit;
    }


}
