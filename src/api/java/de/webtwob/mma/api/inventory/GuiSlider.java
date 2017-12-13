package de.webtwob.mma.api.inventory;

import de.webtwob.mma.api.APILog;
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
    
    private final int sliderLength;
    private final ResourceLocation texture;
    private final int enabledTextureX;
    private final int disabledTextureX;
    private final int enabledTextureY;
    private final int disabledTextureY;
    private boolean holding;
    private int startPos = 0;
    private Orientation orientation;
    private int sliderPosition = 0;
    private boolean isOver;
    
    public GuiSlider(int id, Orientation orientation, int xPos, int yPos, int width, int height, int sliderLength, ResourceLocation texture, int enabledTextureX, int enabledTextureY, int disabledTextureX, int disabledTextureY) {
        super(id, xPos, yPos, width, height, "");
        enabled = false;
        this.enabledTextureX = enabledTextureX;
        this.disabledTextureX = disabledTextureX;
        this.enabledTextureY = enabledTextureY;
        this.disabledTextureY = disabledTextureY;
        this.texture = texture;
        this.orientation = orientation;
        this.sliderLength = sliderLength;
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
            APILog.debug(String.format("Bounds x:%d-%d y:%d-%d Actual: x: %d y: %d",xFrom,xTo,yFrom,yTo,mouseX,mouseY));
            if (xFrom <= mouseX && xTo >= mouseX && yFrom <= mouseY && yTo >= mouseY) {
                startPos = sliderPosition - (orientation.horizontal?mouseX:mouseY);
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
        mc.getTextureManager().bindTexture(texture);
        hovered = new Rectangle(x, y, orientation.horizontal ? sliderLength : width, orientation.horizontal ? height : sliderLength).contains(mouseX, mouseY);
        isOver = new Rectangle(x,y,width,height).contains(mouseX,mouseY);
        int textureX = enabled ? enabledTextureX : disabledTextureX;
        int textureY = enabled ? enabledTextureY : disabledTextureY;
        if (orientation.horizontal) {
            drawTexturedModalRect(x + sliderPosition, y, textureX, textureY, sliderLength, height);
        } else {
            drawTexturedModalRect(x, y + sliderPosition, textureX, textureY, width, sliderLength);
        }
    }
    
    
    public double getSliderPosition() {
        if (orientation.horizontal) {
            return sliderPosition / (double) (width - sliderLength);
        } else {
            return sliderPosition / (double) (height - sliderLength);
        }
    }
    
    public void setSliderPosition(double sliderPosition) {
        int max = height-sliderLength;
        if(orientation.horizontal){
            max = width-sliderLength;
        }
        if(sliderPosition>=0&&sliderPosition<=1){
            this.sliderPosition = (int) (max*sliderPosition);
        }
        else{
            if(sliderPosition<0){
                this.sliderPosition = 0;
            }else{
                this.sliderPosition = max;
            }
        }
    }
    
    //call this in handleMouseInput in your GuiContainer or what ever
    public void handleMouseInput(boolean slowScroll) {
        if(isOver) {
            int i = Integer.signum(Mouse.getEventDWheel());
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
