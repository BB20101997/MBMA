package de.webtwob.mbma.api.capability.factory;

import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class CraftingRequestFactory implements Callable<ICraftingRequest> {

    @Override
    public ICraftingRequest call() {
        return new ICraftingRequest() {

            private ItemStack stack = ItemStack.EMPTY;
            private int quantity = -1;

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
        };
    }
}
