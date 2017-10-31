package de.webtwob.mbma.api.interfaces.capability;

import de.webtwob.mbma.api.capability.APICapabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public interface IBlockPosProvider {
    
    static BlockPos getBlockPos(ItemStack stack) {
        IBlockPosProvider bpp = stack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null);
        return bpp != null ? bpp.getBlockPos() : null;
    }
    
    BlockPos getBlockPos();
    
    void setBlockPos(BlockPos pos);
}
