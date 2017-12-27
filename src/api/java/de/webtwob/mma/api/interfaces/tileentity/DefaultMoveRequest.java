package de.webtwob.mma.api.interfaces.tileentity;

import de.webtwob.mma.api.crafting.ItemStackContainer;

import net.minecraft.item.ItemStack;

import java.util.Queue;
import java.util.function.Consumer;

public class DefaultMoveRequest implements IItemMoveRequest {

    private final ItemStack                    request;
    private final ItemStackContainer           container;
    private final Queue<IMoveRequestProcessor> indexers;
    private final Consumer<ItemStackContainer> returnResult;

    public DefaultMoveRequest(final ItemStack stack, final ItemStackContainer isc, Queue<IMoveRequestProcessor> storageIndexers, Consumer<ItemStackContainer> result) {
        request = stack;
        container = isc;
        indexers = storageIndexers;
        returnResult = result;
    }

    @Override
    public Type getType() {
        return request.isEmpty() ? Type.DEPOSIT_ITEMS : Type.REQUEST_ITEMS;
    }

    @Override
    public ItemStackContainer getItemContainer() {
        return container;
    }

    @Override
    public ItemStack getRequest() {
        return request;
    }

    @Override
    public void passOnRequest() {
        if (getType() == Type.DEPOSIT_ITEMS && container.getItemStack().isEmpty()) {
            container.returnToPool();
        } else if (getType() == Type.REQUEST_ITEMS && request.getCount() <= container.getItemStack().getCount()) {
            //we succeeded in gathering the requested Items
            returnResult.accept(container);
        } else {
            if (indexers.isEmpty()) {
                returnResult.accept(container);
            } else {
                indexers.poll().addItemMoveRequest(this);
            }
        }
    }
}
