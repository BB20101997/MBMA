package de.webtwob.mbma.api.util.multiblock;

import de.webtwob.mbma.api.interfaces.tileentity.IMultiBlockTile;
import de.webtwob.mbma.api.multiblock.MultiBlockGroup;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class MultiBlockUtils {
    
    private MultiBlockUtils() {
    }
    
    public static MultiBlockGroup getGroupForPosition(World world, BlockPos pos){
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IMultiBlockTile) {
            return ((IMultiBlockTile) tileEntity).getGroup();
        }
        return null;
    }
    
}
