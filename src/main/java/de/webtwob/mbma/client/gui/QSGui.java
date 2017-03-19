package de.webtwob.mbma.client.gui;

import de.webtwob.mbma.common.inventory.QSContainer;
import de.webtwob.mbma.common.references.MBMAResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
public class QSGui extends GuiContainer {


    private static final int              xL          = 176, yL = 220;

    private final IInventory   player;
    private final IItemHandler qs;

    public QSGui(InventoryPlayer playerInv, IItemHandler qsInventory) {
        super(new QSContainer(playerInv, qsInventory, Minecraft.getMinecraft().player));
        xSize = 176;
        ySize = 220;
        player = playerInv;
        qs = qsInventory;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(MBMAResources.GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
    }
}
