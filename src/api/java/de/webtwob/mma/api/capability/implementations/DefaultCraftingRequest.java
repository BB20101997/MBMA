package de.webtwob.mma.api.capability.implementations;

import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 16. Nov. 2017.
 */
public class DefaultCraftingRequest implements ICraftingRequest {

    private ItemStack stack    = ItemStack.EMPTY;
    private int       quantity = -1;

    @Nonnull
    @Override
    public ItemStack getRequest() {
        return stack;
    }

    @Override
    public void setRequest(@Nonnull ItemStack itemStack) {
        stack = itemStack;
    }

    @Override
    public boolean isCompleted() {
        return quantity <= 0 || stack.isEmpty();
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(final int amount) {
        quantity = amount;
    }

    @Override
    public void reduceQuantity(final int i) {
        quantity -= 1;
    }
}
