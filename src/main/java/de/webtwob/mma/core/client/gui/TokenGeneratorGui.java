package de.webtwob.mma.core.client.gui;

import de.webtwob.mma.core.common.inventory.TokenGeneratorContainer;
import de.webtwob.mma.core.common.references.ResourceLocations;
import de.webtwob.mma.core.common.tileentity.TileEntityRequestGenerator;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by bennet on 28.03.17.
 */
public class TokenGeneratorGui extends GuiContainer {

    EntityPlayer player;
    TileEntityRequestGenerator tileEntity;
    int pageScroll = 0;

    public TokenGeneratorGui(EntityPlayer player0, TileEntityRequestGenerator tgte) {
        super(new TokenGeneratorContainer(player0, tgte));
        player = player0;
        xSize = 230;
        ySize = 168;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(ResourceLocations.Textures.TOKEN_GENERATOR_GUI);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
