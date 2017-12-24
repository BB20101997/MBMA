package de.webtwob.mma.api.interfaces.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by BB20101997 on 17. Okt. 2017.
 */
@FunctionalInterface
public interface IDebuggableBlock {

    /**
     * This method is called on Blocks that implement this Interface when the DebugWand is right-clicked on it
     *  @param world  the world this block is in
     * @param pos    the position this block is at
     * @param player the player that caused this interaction
     */
    void performDebugOnBlock(World world, BlockPos pos, EntityPlayer player);

}
