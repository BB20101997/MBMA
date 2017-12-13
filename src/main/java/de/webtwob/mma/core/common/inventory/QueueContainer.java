package de.webtwob.mma.core.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 13. Dez. 2017.
 */
public class QueueContainer extends Container {
    
    
    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }
    
    public static QueueContainer tryCreateInstance(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new QueueContainer();
    }
}
