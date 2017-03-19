package de.webtwob.mbma.api.capability.interfaces;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public interface ICraftingRequest {


    @Nonnull
    ItemStack getRequest();

    void setRequest(@Nonnull ItemStack stack);

    default boolean isCompleted() {
        return getRequest().isEmpty();
    }

    default void reduceQuantity(int i) {
        getRequest().shrink(i);
    }

}
