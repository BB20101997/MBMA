package de.webtwob.mma.core.common.block;

import de.webtwob.mma.api.inventory.ApiCommonProxy;
import de.webtwob.mma.core.common.references.ObjectHolders;
import de.webtwob.mma.core.common.tileentity.TileEntityRequestGenerator;

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
 * Created by bennet on 23.03.17.
 */
public class BlockRequestGenerator extends MMABlock{

    public BlockRequestGenerator(ResourceLocation rl) {
        super(Material.IRON,rl);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing dir, float x, float y, float z) {
        //open gui
        if (!world.isRemote) {
            player.openGui(ObjectHolders.apiInstance, ApiCommonProxy.TILE_ENTITY_GUI,world,blockPos.getX(),blockPos.getY(),blockPos.getZ());
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityRequestGenerator();
    }
}
