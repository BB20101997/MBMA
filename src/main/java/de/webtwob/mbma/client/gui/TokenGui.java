package de.webtwob.mbma.client.gui;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.MBMAPacketHandler;
import de.webtwob.mbma.common.inventory.TokenContainer;
import de.webtwob.mbma.common.packet.TokenUpdatePacket;
import de.webtwob.mbma.common.references.MBMAResources;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by bennet on 21.03.17.
 */
public class TokenGui extends GuiContainer {

    private final EntityPlayer player;
    ItemStack token;

    private GuiButton saveButton;
    private GuiTextField itemNameTextField;

    public TokenGui(ItemStack stack, EntityPlayer player) {
        super(new TokenContainer(stack));
        token = stack;
        this.player = player;
        xSize = 139;
        ySize = 50;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        itemNameTextField = new GuiTextField(0, fontRendererObj, guiLeft+10, guiTop+10, 110, 16);
        itemNameTextField.setTextColor(-1);
        itemNameTextField.setDisabledTextColour(-1);
        itemNameTextField.setEnableBackgroundDrawing(false);
        ICraftingRequest icr = token.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST,null);
        if(icr!=null){
            ItemStack req = icr.getRequest();
            if(!req.isEmpty()){
                itemNameTextField.setText(req.getItem().getRegistryName().toString());
            }
        }
        saveButton = new GuiButton(0, guiLeft + 10, guiTop + 30, "Save");
        addButton(saveButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!itemNameTextField.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        itemNameTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        itemNameTextField.drawTextBox();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(MBMAResources.TOKEN_GUI);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawTexturedModalRect(guiLeft + 10, guiTop + 10, 0, ySize + (itemNameTextField.isFocused() ? 0 : 16), 110, 16);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
            MBMALog.debug("Saving Token!");
            updateToken();
        }
    }

    private void updateToken() {
        Item request = Item.getByNameOrId(itemNameTextField.getText());
        ItemStack requestStack;
        if(request==null){
            requestStack = ItemStack.EMPTY;
        }else{
            requestStack = new ItemStack(request,1);
        }
        MBMAPacketHandler.INSTANCE.sendToServer(new TokenUpdatePacket(requestStack));
    }
}
