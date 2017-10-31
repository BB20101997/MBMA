package de.webtwob.mbma.core.common.block;


import de.webtwob.mbma.core.common.tileentity.TileEntityStorageIndexer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 31. Okt. 2017.
 */
public class BlockStorageIndexer extends Block {
    
    
    
    public BlockStorageIndexer(){
        super(Material.IRON);
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityStorageIndexer();
    }
    
    
}
