package de.webtwob.mbma.api.interfaces.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by BB20101997 on 17. Okt. 2017.
 */
@FunctionalInterface
public interface IDebugableBlock {
    
    void performDebugOnBlock(World world, BlockPos pos, EntityPlayer player, int flag);
    
}
