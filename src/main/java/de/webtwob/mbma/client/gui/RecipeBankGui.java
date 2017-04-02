package de.webtwob.mbma.client.gui;

import de.webtwob.mbma.common.MBMAPacketHandler;
import de.webtwob.mbma.common.inventory.RecipeBankContainer;
import de.webtwob.mbma.common.packet.PageUpdatePacket;
import de.webtwob.mbma.common.references.MBMAResources;
import de.webtwob.mbma.common.tileentity.RecipeBankTileEntity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

/**
 * Created by bennet on 01.04.17.
 */
public class RecipeBankGui extends GuiContainer {
    
    private RecipeBankContainer  container;
    private RecipeBankTileEntity te;
    private int         pageScrollOffset = 0;
    private GuiButton[] pages            = new GuiButton[5];
    
    private static final int maxOffset      = 36;
    private static final int scrollBarStart = 12;
    private static final int scrollerLength = 15;
    
    public RecipeBankGui(EntityPlayer player, RecipeBankTileEntity tileEntity) {
        super(new RecipeBankContainer(player, tileEntity));
        container = (RecipeBankContainer) inventorySlots;
        te = tileEntity;
        xSize = 256;
        ySize = 223;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        addButton(new GuiButton(0, guiLeft + xSize, guiTop, 80, 20, "New Page"));
        addButton(new GuiButton(7, guiLeft + xSize, guiTop + 20, 80, 20, "Delete Page"));
        
        addButton(new GuiButton(8, guiLeft + 239, guiTop + scrollBarStart - 6, 10, 5, "^"));
        addButton(
                new GuiButton(9, guiLeft + 239, guiTop + scrollBarStart + maxOffset + scrollerLength + 1, 10, 5, "v"));
        
        for(int i = 0; i < 5; i++){
            addButton(pages[i] = new GuiButton(i + 1, guiLeft + 133, guiTop + 7 + i * 12, 104, 12, ""));
        }
    }
    
    private int calcOffset() {
        return te.getPageCount() < 6 ? 0 : pageScrollOffset < te.getPageCount() - 5 ? pageScrollOffset * maxOffset / (te.getPageCount() - 5) : maxOffset;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(MBMAResources.RECIPE_BANK_GUI);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawTexturedModalRect(guiLeft + 240, guiTop + scrollBarStart + calcOffset(), te.getPageCount() < 6 ? 8 : 0,
                              256 - scrollerLength, 8, scrollerLength
        );
        for(int i = 0; i < 5; i++){
            if(te.getPageCount() > i + pageScrollOffset) {
                RecipeBankTileEntity.RecipePage page = te.getPage(i + pageScrollOffset);
                if("".equals(page.name)) {
                    pages[i].displayString = "Page " + (i + pageScrollOffset);
                } else {
                    pages[i].displayString = page.name;
                }
                pages[i].enabled = true;
            } else {
                pages[i].displayString = "";
                pages[i].enabled = false;
            }
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton action) throws IOException {
        super.actionPerformed(action);
        if(action.id == 0) {
            MBMAPacketHandler.INSTANCE.sendToServer(PageUpdatePacket.createCreatePagePacket(""));
            updateScreen();
        }
        if(action.id > 0 && action.id < 6) {
            MBMAPacketHandler.INSTANCE.sendToServer(
                    PageUpdatePacket.createSwichPagePacket(action.id - 1 + pageScrollOffset));
        }
        if(action.id == 7) {
            MBMAPacketHandler.INSTANCE.sendToServer(PageUpdatePacket.createDeletePagePacket());
        }
        if(action.id == 8) {
            pageScrollOffset = Math.max(0, pageScrollOffset - 1);
        }
        if(action.id == 9) {
            pageScrollOffset = Math.max(Math.min(pageScrollOffset + 1, te.getPageCount() - 5), 0);
        }
    }
}
