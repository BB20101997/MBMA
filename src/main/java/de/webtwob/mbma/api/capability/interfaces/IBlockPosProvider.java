package de.webtwob.mbma.api.capability.interfaces;

import net.minecraft.util.math.BlockPos;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public interface IBlockPosProvider {

    BlockPos getBlockPos();

    void setBlockPos(BlockPos pos);
}
