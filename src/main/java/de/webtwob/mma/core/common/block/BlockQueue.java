package de.webtwob.mma.core.common.block;

import de.webtwob.mma.api.inventory.ApiCommonProxy;
import de.webtwob.mma.core.common.references.ObjectHolders;
import de.webtwob.mma.core.common.tileentity.TileEntityQueue;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 06. Nov. 2017.
 */
public class BlockQueue extends MMABlock {
    
    public BlockQueue(ResourceLocation rl) {
        super(Material.IRON,rl);
    }
   
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(ObjectHolders.apiInstance, ApiCommonProxy.TILE_ENTITY_GUI,world,pos.getX(),pos.getY(),pos.getZ());
        }
        return true;
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
