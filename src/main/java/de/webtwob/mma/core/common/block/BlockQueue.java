package de.webtwob.mma.core.common.block;

import de.webtwob.mma.core.common.creativetab.MMACreativeTab;
import de.webtwob.mma.core.common.tileentity.TileEntityQueue;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 06. Nov. 2017.
 */
public class BlockQueue extends Block {
    
    public BlockQueue() {
        super(Material.IRON);
        
        setCreativeTab(MMACreativeTab.MMATab);
    }
    
    @Override
    public boolean hasTileEntity(final IBlockState state) {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(
            @Nonnull final World world, @Nonnull final IBlockState state
    ) {
        return new TileEntityQueue();
    }
}
