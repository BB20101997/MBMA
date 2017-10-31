package de.webtwob.mbma.api.capability.factory;

import de.webtwob.mbma.api.interfaces.capability.IBlockPosProvider;

import net.minecraft.util.math.BlockPos;

import java.util.concurrent.Callable;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class BlockPosFactory implements Callable<IBlockPosProvider> {
    
    @Override
    public IBlockPosProvider call() {
        return new IBlockPosProvider() {
            
            private BlockPos pos = null;
            
            @Override
            public BlockPos getBlockPos() {
                return pos;
            }
            
            @Override
            public void setBlockPos(BlockPos pos) {
                this.pos = pos;
            }
        };
    }
}
