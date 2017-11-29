package de.webtwob.mma.api.interfaces.tileentity;

import de.webtwob.mma.api.crafting.ItemStackContainer;

import net.minecraft.item.ItemStack;

/**
 * Created by BB20101997 on 31. Okt. 2017.
 */
public interface IItemMoveRequest {

    enum Type{
        REQUEST_ITEMS,
        DEPOSIT_ITEMS
    }


    /**
     * If the items should be stored away or Items should be gathered
     * */
    Type getType();

    /**
     * The Container to store the result in
     * or the Container storing the Items to dump
     * Even for requests this may not be empty as some other Entitsy might have already tried to gather this
     * then try to get what is missing
     * */
    ItemStackContainer getItemContainer();

    /**
     * for Type REQUEST_ITEMS only
     * get the requested ItemStack
     * try to get as much of it as possible but at max the size of the stack
     * */
    ItemStack getRequest();

    /**
     * Tell the requesting Entity that you have done all you could do
     * if not full filled will probably pass this on to the next Entity that may handle this request
     * */
    void passOnRequest();

}
