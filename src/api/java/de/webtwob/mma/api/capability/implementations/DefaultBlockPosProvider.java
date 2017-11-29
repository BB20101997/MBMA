package de.webtwob.mma.api.capability.implementations;

import de.webtwob.mma.api.interfaces.capability.IBlockPosProvider;

import net.minecraft.util.math.BlockPos;

/**
 * Created by BB20101997 on 16. Nov. 2017.
 */
public class DefaultBlockPosProvider implements IBlockPosProvider {
    
    private BlockPos pos = null;
    
    @Override
    public BlockPos getBlockPos() {
        return pos;
    }
    
    @Override
    public void setBlockPos(BlockPos pos) {
        this.pos = pos;
    }
}
