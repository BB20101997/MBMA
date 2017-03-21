package de.webtwob.mbma.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

/**
 * Created by bennet on 21.03.17.
 */
public class TokenContainer extends Container {

    public ItemStack stack;

    public TokenContainer(ItemStack stack) {
        this.stack = stack;
    }



    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
