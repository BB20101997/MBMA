package de.webtwob.mbma.client.gui;

import de.webtwob.mbma.common.inventory.TokenGeneratorContainer;
import de.webtwob.mbma.common.references.MBMAResources;
import de.webtwob.mbma.common.tileentity.TokenGeneratorTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by bennet on 28.03.17.
 */
public class TokenGeneratorGui extends GuiContainer {

    EntityPlayer player;

    public TokenGeneratorGui(EntityPlayer player0, TokenGeneratorTileEntity tgte) {
        super(new TokenGeneratorContainer(player0,tgte));
        player = player0;
        xSize = 230;
        ySize = 168;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(MBMAResources.TOKEN_GENERATOR_GUI);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
