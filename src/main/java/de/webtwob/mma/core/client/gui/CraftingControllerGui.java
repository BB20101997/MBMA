package de.webtwob.mma.core.client.gui;

import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mma.api.inventory.GuiSlider;
import de.webtwob.mma.core.common.inventory.CraftingControllerContainer;
import de.webtwob.mma.core.common.references.ResourceLocations;
import de.webtwob.mma.core.common.tileentity.TileEntityCraftingController;

import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.io.IOException;

/**
 * Created by BB20101997 on 03. Dez. 2017.
 */
public class CraftingControllerGui extends GuiContainer {

    private static final int ELEMENT_HEIGHT = 12;
    private TileEntityCraftingController tileEntityCraftingController;
    private GuiSlider slider     = new GuiSlider(0, GuiSlider.Orientation.VERTICAL, 0, 0, 103);
    private int       selected   = -1;
    private int       listLength = 0;

    public CraftingControllerGui(EntityPlayer player, TileEntityCraftingController tecc) {
        super(new CraftingControllerContainer(player, tecc));
        tileEntityCraftingController = tecc;
        xSize = 176;
        ySize = 220;
    }

    public static CraftingControllerGui tryCreateInstance(final EntityPlayer player, final TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityCraftingController) {
            return new CraftingControllerGui(player, (TileEntityCraftingController) tileEntity);
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
        mc.getTextureManager().bindTexture(ResourceLocations.Textures.LINKING_INTERFACE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        NonNullList<ItemStack> linkList = tileEntityCraftingController.getQueueLinkCards();
        if (selected >= linkList.size()) {
            selected = -1;
        }
        listLength = linkList.size();
        int offset = slider.sliderPositionToRange(0, getMaxOffset());
        for (int i = offset / ELEMENT_HEIGHT; i < listLength; i++) {
            if (i * ELEMENT_HEIGHT - offset < 104) {
                mc.getTextureManager().bindTexture(de.webtwob.mma.api.references.ResourceLocations.GUI_COMPONENTS);
                if (selected != i) {
                    //not selected
                    drawTexturedModalRect(guiLeft + 8, guiTop + 29 + i * ELEMENT_HEIGHT - offset, 0, 18, 147,
                                          ELEMENT_HEIGHT
                    );
                } else {
                    //selected
                    drawTexturedModalRect(
                            guiLeft + 8, guiTop + 29 + i * ELEMENT_HEIGHT - offset, 0, 30, 147, ELEMENT_HEIGHT);
                }
                ItemStack stack = linkList.get(i);
                BlockPos  pos   = IBlockPosProvider.getBlockPos(stack);
                String    text;
                if (pos != null) {
                    text = String.format("%2d: X: %d Y: %d Z: %d", i, pos.getX(), pos.getY(), pos.getZ());
                } else {
                    text = i + ": " + stack.getDisplayName();
                }
                GuiLabel label = new GuiLabel(fontRenderer, i, guiLeft + 10, guiTop + 31 + i * ELEMENT_HEIGHT - offset,
                                              144, 10, Color.WHITE.getRGB()
                );
                label.addLine(text);
                label.drawLabel(mc, mouseX, mouseY);
            }
        }
        //we may have drawn over some parts of the gui so we redraw those
        mc.getTextureManager().bindTexture(ResourceLocations.Textures.LINKING_INTERFACE);
        drawTexturedModalRect(guiLeft, guiTop + 132, 0, 132, xSize, ELEMENT_HEIGHT);
        drawTexturedModalRect(guiLeft, guiTop + 17, 0, 17, xSize, ELEMENT_HEIGHT);
        slider.setEnabled(linkList.size() >= 9);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isPointInRegion(9, 30, 145, 102, mouseX, mouseY)) {
            selected = (mouseY + slider.sliderPositionToRange(0, getMaxOffset()) - guiTop - 29) / ELEMENT_HEIGHT;
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

    private int getMaxOffset() {
        return Math.max(0, (listLength - 8) * ELEMENT_HEIGHT - 7);
    }

}
