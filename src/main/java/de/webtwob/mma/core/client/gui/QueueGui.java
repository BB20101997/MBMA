package de.webtwob.mma.core.client.gui;

import de.webtwob.mma.api.crafting.ItemStackContainer;
import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mma.api.inventory.GuiSlider;
import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.inventory.QueueContainer;
import de.webtwob.mma.core.common.references.ResourceLocations;
import de.webtwob.mma.core.common.tileentity.TileEntityQueue;

import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by BB20101997 on 13. Dez. 2017.
 */
public class QueueGui extends GuiContainer {
    
    private static final int ELEMENT_HEIGHT = 16;//TODO
    private TileEntityQueue entityQueue;
    private GuiSlider slider     = new GuiSlider(0, GuiSlider.Orientation.VERTICAL, 0, 0, 103);
    private int       selected   = -1;
    private int       listLength = 0;
    
    private QueueGui(TileEntityQueue te,EntityPlayer player) {
        super(new QueueContainer(te,player));
        entityQueue = te;
        xSize = 176;
        ySize = 220;
    }
    
    public static QueueGui tryCreateInstance(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityQueue) {
            return new QueueGui((TileEntityQueue) te,player);
        }
        return null;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        slider.x = guiLeft + 160;
        slider.y = guiTop + 29;
        addButton(slider);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(ResourceLocations.Textures.QUEUES_GUI);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        List<ItemStackContainer> queue = entityQueue.getCurrentRequests();
        listLength = queue.size();
        if (selected >= queue.size()) {
            selected = -1;
        }
        int offset = slider.sliderPositionToRange(0,getMaxOffset());
        for (int i = offset/ ELEMENT_HEIGHT; i < listLength; i++) {
            if (104 > i * ELEMENT_HEIGHT - offset) {
                mc.getTextureManager().bindTexture(de.webtwob.mma.api.references.ResourceLocations.GUI_COMPONENTS);
                if (selected != i) {
                    //not selected
                    drawTexturedModalRect(guiLeft + 8, guiTop + 29 + i * ELEMENT_HEIGHT - offset, 0, 18, 147, ELEMENT_HEIGHT);
                } else {
                    //selected
                    drawTexturedModalRect(guiLeft + 8, guiTop + 29 + i * ELEMENT_HEIGHT - offset, 0, 30, 147, ELEMENT_HEIGHT);
                }
                ItemStack stack = queue.get(i).getItemStack();
                BlockPos pos = IBlockPosProvider.getBlockPos(stack);
                String text;
                if (null != pos) {
                    text = String.format("%2d: X: %d Y: %d Z: %d", i, pos.getX(), pos.getY(), pos.getZ());
                } else {
                    text = i + ": " + stack.getDisplayName();
                }
                GuiLabel label = new GuiLabel(fontRenderer, i, guiLeft + 10, guiTop + 31 + i * ELEMENT_HEIGHT - offset, 144, 10, Color.WHITE.getRGB());
                label.addLine(text);
                label.drawLabel(mc, mouseX, mouseY);
            }
        }
        //we may have drawn over some parts of the gui so we redraw those
        mc.getTextureManager().bindTexture(ResourceLocations.Textures.LINKING_INTERFACE);
        drawTexturedModalRect(guiLeft, guiTop + 132, 0, 132, xSize, ELEMENT_HEIGHT);
        drawTexturedModalRect(guiLeft, guiTop + 17, 0, 17, xSize, ELEMENT_HEIGHT);
        slider.setEnabled(9 <= queue.size());
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isPointInRegion(9, 30, 145, 102, mouseX, mouseY)) {
            selected = (mouseY + slider.sliderPositionToRange(0,getMaxOffset()) - guiTop - 29) / ELEMENT_HEIGHT;
        }
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        slider.mouseDragged(mc, mouseX, mouseY);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        slider.handleMouseInput(isShiftKeyDown());
    }
   
    private int getMaxOffset(){
        return Math.max(0,(listLength - 8) * ELEMENT_HEIGHT - 7);
    }
}
