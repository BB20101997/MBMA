package de.webtwob.mbma.api.interfaces;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by bennet on 27.03.17.
 */
public interface IStackFilter extends IObjectCondition<ItemStack> {
    boolean accept(ItemStack stack);
    
    @Override
    default boolean checkCondition(@Nonnull ItemStack object){
        return accept(object);
    }
}
