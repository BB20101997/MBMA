package de.webtwob.mbma.core.client.gui;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.api.inventory.GhostSlot;
import de.webtwob.mbma.core.common.MBMALog;
import de.webtwob.mbma.core.common.inventory.TokenContainer;
import de.webtwob.mbma.core.common.packet.TokenUpdatePacket;
import de.webtwob.mbma.core.common.references.MBMAResourceLocations;
import de.webtwob.mbma.core.common.registration.MBMAPacketHandler;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * Created by bennet on 21.03.17.
 */
public class TokenGui extends GuiContainer {

    private final EntityPlayer player;
    private ItemStack token;
    private int amount = 1;

    private GuiButton saveButton;
    private GuiTextField itemNameTextField;
    private GuiButton itemCountUp, itemCountUp10;
    private GuiButton itemCountDown, itemCountDown10;
    private GhostSlot slot;

    public TokenGui(ItemStack stack, EntityPlayer player) {
        super(new TokenContainer(stack));
        slot = new GhostSlot(guiLeft + 114, guiTop + 28);
        token = stack;
        this.player = player;
        xSize = 130;
        ySize = 56;
    }

    @Override
    public void initGui() {
        super.initGui();

        slot.xPos = 103;
        slot.yPos = 32;
        ((TokenContainer) inventorySlots).addSlotToContainer(slot);
        Keyboard.enableRepeatEvents(true);
        itemNameTextField = new GuiTextField(0, fontRenderer, guiLeft + 13, guiTop + 14, 104, 16);
        itemNameTextField.setTextColor(-1);
        itemNameTextField.setDisabledTextColour(-1);
        itemNameTextField.setEnableBackgroundDrawing(false);
        ICraftingRequest icr = token.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null);
        if (icr != null) {
            amount = icr.getQuantity();
            ResourceLocation loc = icr.getRequest().getItem().getRegistryName();
            if (loc != null) {
                itemNameTextField.setText(loc.toString());
            }
        }
        saveButton = new GuiButton(0, guiLeft + 10, guiTop + ySize + 5, xSize - 20, 20, "Save");
        addButton(saveButton);

        itemCountUp = new GuiButton(1, guiLeft + 90, guiTop + 29, 10, 10, "+");
        itemCountDown = new GuiButton(2, guiLeft + 90, guiTop + 41, 10, 10, "-");
        itemCountUp10 = new GuiButton(3, guiLeft + 68, guiTop + 29, 20, 10, "++");
        itemCountDown10 = new GuiButton(4, guiLeft + 68, guiTop + 41, 20, 10, "--");
        addButton(itemCountUp);
        addButton(itemCountDown);
        addButton(itemCountUp10);
        addButton(itemCountDown10);

        updateItemStack();
    }

    private void updateItemStack() {
        if (amount < 1) {
            amount = 1;
        }
        Item item = Item.getByNameOrId(itemNameTextField.getText());
        if (item != null) {
            slot.setItemStack(new ItemStack(item, amount));
        } else {
            slot.setItemStack(ItemStack.EMPTY);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!itemNameTextField.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        } else {
            updateItemStack();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        itemNameTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(MBMAResourceLocations.Textures.TOKEN_GUI);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawTexturedModalRect(guiLeft + 10, guiTop + 10, 0, ySize + (itemNameTextField.isFocused() ? 0 : 16), 110, 16);
        itemNameTextField.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: {
                MBMALog.debug("Saving Token!");
                updateToken();
                break;
            }
            case 1: {
                amount++;
                break;
            }
            case 2: {
                amount--;
                break;
            }
            case 3: {
                if (amount == 1) {
                    amount = 10;
                } else {
                    amount += 10;
                }
                break;
            }
            case 4: {
                amount -= 10;
                break;
            }
        }
        updateItemStack();
    }

    private void updateToken() {
        Item request = Item.getByNameOrId(itemNameTextField.getText());
        ItemStack requestStack;
        if (request == null) {
            requestStack = ItemStack.EMPTY;
        } else {
            requestStack = new ItemStack(request);
        }
        MBMAPacketHandler.INSTANCE.sendToServer(new TokenUpdatePacket(requestStack, amount));
    }
}
