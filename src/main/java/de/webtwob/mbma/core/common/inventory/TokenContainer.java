package de.webtwob.mbma.core.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by bennet on 21.03.17.
 */
public class TokenContainer extends Container {

    public ItemStack stack;

    public TokenContainer(ItemStack stack) {
        this.stack = stack;
    }

    @Nonnull
    @Override
    public Slot addSlotToContainer(Slot slotIn) {
        return super.addSlotToContainer(slotIn);
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }
}
