package de.webtwob.mbma.common.inventory;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by bennet on 22.03.17.
 */
public class GhostSlot extends Slot {

    private ItemStack stack = ItemStack.EMPTY;

    public GhostSlot(int xPosition, int yPosition) {
        super(null, 0, xPosition, yPosition);
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void putStack(ItemStack stack) {
    }

    public void setItemStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void onSlotChanged() {
    }

    @Override
    public int getSlotStackLimit() {
        return 0;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 0;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isHere(IInventory inv, int slotIn) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return false;
    }

    @Override
    public boolean canBeHovered() {
        return super.canBeHovered();
    }

    @Override
    public int getSlotIndex() {
        return 0;
    }

    @Override
    public boolean isSameInventory(Slot other) {
        return false;
    }
}
