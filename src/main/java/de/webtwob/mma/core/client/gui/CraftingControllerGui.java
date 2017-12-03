package de.webtwob.mma.core.client.gui;

import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mma.core.common.inventory.CraftingControllerContainer;
import de.webtwob.mma.core.common.references.ResourceLocations;
import de.webtwob.mma.core.common.tileentity.TileEntityCraftingController;

import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.io.IOException;

/**
 * Created by BB20101997 on 03. Dez. 2017.
 */
public class CraftingControllerGui extends GuiContainer {
    
    TileEntityCraftingController tileEntityCraftingController;
    
    int selected = -1;
    int offset = 0;
    int sliderOffset = 0;
    
    public CraftingControllerGui(EntityPlayer player, TileEntityCraftingController tecc) {
        super(new CraftingControllerContainer(player, tecc));
        tileEntityCraftingController = tecc;
        xSize = 176;
        ySize = 220;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(ResourceLocations.Textures.LINKING_INTERFACE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        NonNullList<ItemStack> linkList = tileEntityCraftingController.getQueueLinkCards();
        if (selected >= linkList.size()) {
            selected = -1;
        }
        
        for (int i = offset/12; i < linkList.size(); i++) {
            if (i * 12 - offset <108) {
                mc.getTextureManager().bindTexture(ResourceLocations.Textures.GUI_COMPONENTS);
                if (selected != i) {
                    //not selected
                    drawTexturedModalRect(guiLeft + 8, guiTop + 29 + i * 12, 0, 18, 147, 12);
                } else {
                    //selected
                    drawTexturedModalRect(guiLeft + 8, guiTop + 29 + i * 12, 0, 30, 147, 12);
                }
                ItemStack stack = linkList.get(i);
                BlockPos pos = IBlockPosProvider.getBlockPos(stack);
                String text;
                if (pos != null) {
                    text = String.format("%2d: X: %d Y: %d Z: %d", i, pos.getX(), pos.getY(), pos.getZ());
                } else {
                    text = i + ": " + stack.getDisplayName();
                }
                GuiLabel label = new GuiLabel(fontRenderer, i, guiLeft + 10, guiTop + 31 + i * 12, 144, 10, Color.WHITE.getRGB());
                label.addLine(text);
                label.drawLabel(mc, mouseX, mouseY);
            }
        }
        //we may have drawn over some parts of the gui so we redraw those
        mc.getTextureManager().bindTexture(ResourceLocations.Textures.LINKING_INTERFACE);
        drawTexturedModalRect(guiLeft, guiTop + 132, 0, 132, xSize, 12);
        drawTexturedModalRect(guiLeft, guiTop + 17, 0, 17, xSize, 12);
    
        mc.getTextureManager().bindTexture(ResourceLocations.Textures.GUI_COMPONENTS);
        if(linkList.size()<9){
        
        }else{
        
        }
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isPointInRegion(9, 30, 145, 102, mouseX, mouseY)) {
            selected = (mouseY + offset - guiTop - 29) / 12;
        }
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if(isPointInRegion()){
        
        }
    }
}
