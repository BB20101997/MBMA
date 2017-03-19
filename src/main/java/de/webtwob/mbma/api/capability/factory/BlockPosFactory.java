package de.webtwob.mbma.api.capability.factory;

import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.Callable;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class BlockPosFactory implements Callable<IBlockPosProvider> {

    @Override
    public IBlockPosProvider call() throws Exception {
        return new IBlockPosProvider() {

            private BlockPos pos = null;

            @Override
            public void setBlockPos(BlockPos pos) {
                this.pos = pos;
            }

            @Override
            public BlockPos getBlockPos() {
                return pos;
            }
        };
    }
}
