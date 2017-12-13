package de.webtwob.mma.api.inventory;

import de.webtwob.mma.api.APILog;
import de.webtwob.mma.api.references.ResourceLocations;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * Created by BB20101997 on 04. Dez. 2017.
 */
public class GuiSlider extends GuiButton {
    
    private int sliderLength = 15;
    private ResourceLocation textureEnabled = ResourceLocations.GUI_COMPONENTS;
    private ResourceLocation textureDisabled = ResourceLocations.GUI_COMPONENTS;
    private int enabledTextureX;
    private int disabledTextureX;
    private int enabledTextureY;
    private int disabledTextureY;
    private boolean holding;
    private int startPos = 0;
    private Orientation orientation = Orientation.VERTICAL;
    private int sliderPosition = 0;
    private boolean isOver;
    
    public GuiSlider(int id, Orientation orientation, int x, int y, int length) {
        super(id, x, y, "");
        if (orientation.horizontal) {
            enabledTextureX = 116;
            enabledTextureY = 0;
            disabledTextureX = 116;
            disabledTextureY = 8;
            width = length;
            this.height = 8;
        } else {
            enabledTextureX = 100;
            enabledTextureY = 0;
            disabledTextureX = 108;
            disabledTextureY = 0;
            width = 8;
            this.height = length;
        }
    }
    
    public void changeDiplaySettings(Orientation orientation, ResourceLocation textureEnabled, ResourceLocation textureDisabled, int enabledX, int enabledY, int disabledX, int disabledY) {
        this.orientation = orientation;
        this.textureEnabled = textureEnabled;
        this.textureDisabled = textureDisabled;
        enabledTextureX = enabledX;
        enabledTextureY = enabledY;
        disabledTextureX = disabledX;
        disabledTextureY = disabledY;
    }
    
    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        super.mouseReleased(mouseX, mouseY);
        holding = false;
    }
    
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (enabled) {
            int xFrom = x;
            int xTo = x + width;
            int yFrom = y;
            int yTo = y + width;
            if (orientation.horizontal) {
                xFrom = x + sliderPosition;
                xTo = xFrom + sliderLength;
            } else {
                yFrom = y + sliderPosition;
                yTo = yFrom + sliderLength;
            }
            APILog.debug(String.format("Bounds x:%d-%d y:%d-%d Actual: x: %d y: %d", xFrom, xTo, yFrom, yTo, mouseX, mouseY));
            if (xFrom <= mouseX && xTo >= mouseX && yFrom <= mouseY && yTo >= mouseY) {
                startPos = sliderPosition - (orientation.horizontal ? mouseX : mouseY);
                holding = true;
                return true;
            }
        }
        return false;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @Override
    public void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        super.mouseDragged(mc, mouseX, mouseY);
        if (!holding) return;
        sliderPosition = startPos + (orientation.horizontal ? mouseX : mouseY);
        sliderPosition = Math.min(Math.max(sliderPosition, 0), orientation.horizontal ? width : height - sliderLength);
    }
    
    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTick) {
        if (!visible) return;
        hovered = new Rectangle(x, y, orientation.horizontal ? sliderLength : width, orientation.horizontal ? height : sliderLength).contains(mouseX, mouseY);
        isOver = new Rectangle(x, y, width, height).contains(mouseX, mouseY);
        mc.getTextureManager().bindTexture(enabled ? textureEnabled : textureDisabled);
        int textureX = enabled ? enabledTextureX : disabledTextureX;
        int textureY = enabled ? enabledTextureY : disabledTextureY;
        if (orientation.horizontal) {
            drawTexturedModalRect(x + sliderPosition, y, textureX, textureY, sliderLength, height);
        } else {
            drawTexturedModalRect(x, y + sliderPosition, textureX, textureY, width, sliderLength);
        }
    }
    
    /**
     * @param from the ranges start
     * @param to   the ranges end
     */
    public int sliderPositionToRange(int from, int to) {
        return (int) ((to - from) * getSliderPosition() + from);
    }
    
    public double getSliderPosition() {
        if (orientation.horizontal) {
            return sliderPosition / (double) (width - sliderLength);
        } else {
            return sliderPosition / (double) (height - sliderLength);
        }
    }
    
    public void setSliderPosition(double sliderPosition) {
        int max = height - sliderLength;
        if (orientation.horizontal) {
            max = width - sliderLength;
        }
        if (sliderPosition >= 0 && sliderPosition <= 1) {
            this.sliderPosition = (int) (max * sliderPosition);
        } else {
            if (sliderPosition < 0) {
                this.sliderPosition = 0;
            } else {
                this.sliderPosition = max;
            }
        }
    }
    
    //call this in handleMouseInput in your GuiContainer or what ever
    public void handleMouseInput(boolean slowScroll) {
        if (isOver) {
            int i = Integer.signum(-Mouse.getEventDWheel());
            int max = height - sliderLength;
            if (orientation.horizontal) {
                max = width - sliderLength;
            }
            if (!slowScroll) {
                i *= 10;
            }
            sliderPosition = Math.max(0, Math.min(sliderPosition + i, max));
        }
    }
    
    public enum Orientation {
        HORIZONTAL(true),
        VERTICAL(false);
        
        public final boolean horizontal;
        
        Orientation(boolean horizontal) {
            this.horizontal = horizontal;
        }
    }
}
