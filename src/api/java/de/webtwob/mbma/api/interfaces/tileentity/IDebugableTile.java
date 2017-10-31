package de.webtwob.mbma.api.interfaces.tileentity;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@FunctionalInterface
public interface IDebugableTile {
    
    void performDebugOnTile(EntityPlayer player);
    
}
