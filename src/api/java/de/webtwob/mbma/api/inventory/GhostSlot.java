package de.webtwob.mbma.api.inventory;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * Created by bennet on 22.03.17.
 */
public class GhostSlot {
    
    private GhostSlot() {
    }
    
    /**
     * draws an ItemStack at the given Coordinates with the given amount
     * @param player the player for whom to draw the GhostSlot
     * @param x the x-Coordinate to draw this ar
     * @param y the y-Coordinate to draw this at
     * @param itemStack the ItemStack to draw
     * @param amount the Item Count to draw, we don't use the itemStacks count since it might not be the actual value we want
     * @param renderer the RenderItem to render the ItemStack with
     * @param fontRenderer the FallBack FontRenderer if we can't get one from the ItemStack
     */
    public static void drawGhostSlot(EntityPlayer player, int x, int y, ItemStack itemStack, int amount, RenderItem renderer, FontRenderer fontRenderer) {
        FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
        if(font==null) font=fontRenderer;
        if (!itemStack.isEmpty()&&amount>0) {
            RenderHelper.enableGUIStandardItemLighting();
            renderer.zLevel = 100;
            GlStateManager.enableDepth();
            renderer.renderItemAndEffectIntoGUI(player, itemStack, x, y);
            renderer.renderItemOverlayIntoGUI(font, itemStack, x, y, amount>1?String.valueOf(amount):"");
            renderer.zLevel = 0;
            GlStateManager.disableDepth();
            RenderHelper.disableStandardItemLighting();
        }
    }
    
    
    /**
     * @param setAmount called to update the amount this GhostSlot holds
     * @param setItem called to update the ItemStack held by this GhostSlot
     * @param item the currently held ItemStack
     * @param amount the currently held amount
     * @param mouseButton the mouse button that was pressed
     * @param player the player that pressed the Button
     */
    public static void adjustCount(IntConsumer setAmount, Consumer<ItemStack> setItem, ItemStack item, int amount, int mouseButton, EntityPlayerSP player) {
        if(mouseButton>1){
            return;
        }
        int diff = 1;
        if(GuiScreen.isShiftKeyDown()){
            diff*=10;
        }
        if(GuiScreen.isCtrlKeyDown()){
            diff*=64;
        }
        if(mouseButton==1) {
            diff*=-1;
        }
        //IDE is sure that stackAtCursor!=null is always true
        ItemStack stackAtCursor = player.inventory.getItemStack();
        if(!stackAtCursor.isEmpty() && !item.equals(stackAtCursor)){
            setItem.accept(stackAtCursor.copy());
            diff = stackAtCursor.getCount()-amount;
        }
        if(diff!=0){
            setAmount.accept(Math.max(amount+diff,0));
        }
    }
}
