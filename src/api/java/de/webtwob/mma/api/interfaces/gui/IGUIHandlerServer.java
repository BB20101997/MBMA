package de.webtwob.mma.api.interfaces.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 11. Dez. 2017.
 */
public interface IGUIHandlerServer {
    
    @Nullable
    Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) ;
    
}
