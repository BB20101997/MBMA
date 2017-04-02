package de.webtwob.mbma.api.capability.implementations;

import de.webtwob.mbma.api.interfaces.IObjectCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Created by bennet on 01.04.17.
 */
public class ReplaceableItemHandler extends FilteredItemHandler {

    public ReplaceableItemHandler(NonNullList<ItemStack> list, IObjectCondition<ItemStack> filter, int maxSize) {
        super(list,filter,maxSize);
    }
    
    public void replaceItemList(NonNullList<ItemStack> list){
        stacks = list;
    }
    
}
