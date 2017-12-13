package de.webtwob.mma.core.client.gui;

import de.webtwob.mma.core.common.inventory.QueueContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by BB20101997 on 13. Dez. 2017.
 */
public class QueueGui extends GuiContainer {
    
    public QueueGui() {
        super(new QueueContainer());
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    
    }
    
    public static QueueGui tryCreateInstance(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new QueueGui();
    }
}
