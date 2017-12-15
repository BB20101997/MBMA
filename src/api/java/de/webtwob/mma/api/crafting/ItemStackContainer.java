package de.webtwob.mma.api.crafting;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Created by BB20101997 on 30. Okt. 2017.
 */
public class ItemStackContainer {

    @Nonnull
    private ItemStack stack = ItemStack.EMPTY;

    private Consumer<ItemStackContainer> consumer;
    private Runnable dirtyCallback = null;

    public ItemStackContainer() {

    }

    public ItemStackContainer(@Nonnull ItemStack stack) {
        this.stack = stack;
    }

    public void markDirty() {
        if (dirtyCallback != null) {
            dirtyCallback.run();
        }
    }

    public void setDirtyCallback(Runnable run) {
        dirtyCallback = run;
    }

    /**
     * get the contained ItemStack, this will not remove it or set it to empty
     */
    @Nonnull
    public ItemStack getItemStack() {
        return stack;
    }

    /**
     * set and override the current ItemStack
     */
    public void setItemStack(@Nonnull ItemStack stack) {
        this.stack = stack;
    }

    /**
     * Set this every time before handing out a container from a pool
     */
    public void setPoolReturn(Consumer<ItemStackContainer> consumer) {
        this.consumer = consumer;
    }

    /**
     * This will return this to the Pool of available ItemStackContainers
     * if the contained ItemStack is not Empty it will be returned to storage
     * be carefully to not dupe items
     * DON'T KEEP ANY REFERENCES TO THIS OBJECT AFTER CALLING THIS METHOD
     */
    public void returnToPool() {
        if (consumer == null) {
            throw new IllegalStateException("Can't return ItemStackContainer to pool,poolReturn not set!");
        }
        consumer.accept(this);
        consumer = null;
    }

}
