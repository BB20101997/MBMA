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
    
    /**
     * draws an ItemStack at the given Coordinates with the given amount
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
    
    public static boolean adjustCount(IntConsumer setAmount, Consumer<ItemStack> setItem, ItemStack item, int amount, int mouseButton, EntityPlayerSP player) {
        boolean dirty = false;
        if(mouseButton>1){
            return false;
        }
        int diff = 1;
        if(GuiScreen.isShiftKeyDown()){
            diff*=10;
        }
        if(GuiScreen.isCtrlKeyDown()){
            diff*=64;
        }
        if(mouseButton==1)
            diff*=-1;
        if(diff!=0){
            setAmount.accept(Math.max(amount+diff,0));
            dirty = true;
        }
        return dirty;
    }
}
