package de.webtwob.mbma.common.interfaces;

import net.minecraft.item.ItemStack;

/**
 * Created by bennet on 27.03.17.
 */
public interface IStackFilter {
    boolean accept(ItemStack stack);
}
