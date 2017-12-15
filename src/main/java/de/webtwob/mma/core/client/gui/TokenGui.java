package de.webtwob.mma.core.client.gui;

import org.lwjgl.input.Keyboard;

import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.api.inventory.GhostSlot;
import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.inventory.TokenContainer;
import de.webtwob.mma.core.common.packet.TokenUpdatePacket;
import de.webtwob.mma.core.common.references.ResourceLocations;
import de.webtwob.mma.core.common.registration.PacketHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.io.IOException;

import static de.webtwob.mma.api.inventory.GhostSlot.adjustCount;

/**
 * Created by bennet on 21.03.17.
 */
public class TokenGui extends GuiContainer {

    private static Capability<ICraftingRequest> requestCapability;
    private int amount = 1;

    private GuiTextField itemNameTextField;

    public TokenGui(EntityPlayer player, EnumHand hand) {
        super(new TokenContainer(player, hand));
        xSize = 130;
        ySize = 56;
    }

    @CapabilityInject(ICraftingRequest.class)
    private static void injectRequest(Capability<ICraftingRequest> requestCapability) {
        TokenGui.requestCapability = requestCapability;
    }

    public static TokenGui tryCreateInstance(final EntityPlayer player, final EnumHand enumHand) {
        ItemStack held = player.getHeldItem(enumHand);
        if (requestCapability != null && held.hasCapability(requestCapability, null)) {
            return new TokenGui(player, enumHand);
        }
        return null;
    }

    @Override
    public void initGui() {
        super.initGui();

        GuiButton saveButton      = new GuiButton(0, guiLeft + 10, guiTop + ySize + 5, xSize - 20, 20, "Save");
        GuiButton itemCountUp10   = new GuiButton(3, guiLeft + 68, guiTop + 29, 20, 10, "++");
        GuiButton itemCountDown10 = new GuiButton(4, guiLeft + 68, guiTop + 41, 20, 10, "--");
        GuiButton itemCountDown   = new GuiButton(2, guiLeft + 90, guiTop + 41, 10, 10, "-");
        GuiButton itemCountUp     = new GuiButton(1, guiLeft + 90, guiTop + 29, 10, 10, "+");

        itemNameTextField = new GuiTextField(0, fontRenderer, guiLeft + 13, guiTop + 14, 104, 16);
        Keyboard.enableRepeatEvents(true);

        itemNameTextField.setTextColor(-1);
        itemNameTextField.setDisabledTextColour(-1);
        itemNameTextField.setEnableBackgroundDrawing(false);

        if (inventorySlots instanceof TokenContainer) {
            TokenContainer container = (TokenContainer) inventorySlots;
            amount = container.getRequestAmount();
            ResourceLocation loc = container.getRequestRegistryName();
            if (loc != null) {
                itemNameTextField.setText(loc.toString());
            }
        }

        addButton(saveButton);

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
        if (inventorySlots instanceof TokenContainer) {
            TokenContainer container = (TokenContainer) inventorySlots;
            container.setItem(Item.getByNameOrId(itemNameTextField.getText()));
            container.setAmount(amount);
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
        if (isPointInRegion(103, 32, 18, 18, mouseX, mouseY)) {
            int    old         = amount;
            String oldResource = itemNameTextField.getText();
            adjustCount(i -> this.amount = i, this::setItem, getItemStackFromTextField(itemNameTextField), amount,
                        mouseButton, this.mc.player
            );
            if (old != amount || !oldResource.equals(itemNameTextField.getText())) {
                updateToken();
            }
        }
        itemNameTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void setItem(ItemStack itemStack) {
        Item             item             = itemStack.getItem();
        ResourceLocation resourceLocation = item.getRegistryName();
        if (resourceLocation != null) {
            itemNameTextField.setText(resourceLocation.toString());
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(ResourceLocations.Textures.TOKEN_GUI);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawTexturedModalRect(guiLeft + 10, guiTop + 10, 0, ySize + (itemNameTextField.isFocused() ? 0 : 16), 110, 16);
        itemNameTextField.drawTextBox();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        GhostSlot.drawGhostSlot(this.mc.player, 103, 32, getItemStackFromTextField(itemNameTextField), amount,
                                itemRender, fontRenderer
        );
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: {
                CoreLog.debug("Saving Token!");
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
            default:
                CoreLog.warn("Unknown actionPerformed in TokenGui");
        }
        updateItemStack();
    }

    private ItemStack getItemStackFromTextField(GuiTextField fiel) {
        Item item = Item.getByNameOrId(fiel.getText());
        if (item == null) {
            return ItemStack.EMPTY;
        } else {
            return new ItemStack(item);
        }
    }

    private void updateToken() {
        PacketHandler.INSTANCE.sendToServer(
                new TokenUpdatePacket(getItemStackFromTextField(itemNameTextField), amount));
    }
}
