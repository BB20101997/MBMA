package de.webtwob.mbma.api.interfaces.tileentity;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@FunctionalInterface
public interface IDebuggableTile {
   
     /**
     * This method is called on TileEntitys that implement this Interface when the DebugWand is right-clicked on it's block
     *
     * @param player the player that caused this interaction
     * */
    void performDebugOnTile(EntityPlayer player);
    
}
