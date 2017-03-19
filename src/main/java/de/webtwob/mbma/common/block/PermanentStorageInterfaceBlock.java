package de.webtwob.mbma.common.block;

import de.webtwob.mbma.api.MBMAProperties;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.tileentity.PSITileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class PermanentStorageInterfaceBlock extends Block {

    public PermanentStorageInterfaceBlock() {
        super(Material.IRON);

        setCreativeTab(MBMACreativeTab.MBMATab);
        setDefaultState(this.blockState.getBaseState().withProperty(MBMAProperties.FACING, EnumFacing.UP)
                                       .withProperty(MBMAProperties.CONNECTED, false));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(MBMAProperties.FACING, EnumFacing.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(MBMAProperties.FACING).ordinal();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return getExtendedState(state, worldIn, pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof PSITileEntity) {
            ((PSITileEntity) tileEntity).update();
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MBMAProperties.FACING, MBMAProperties.CONNECTED);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new PSITileEntity();
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if(pos.offset(world.getBlockState(pos).getValue(MBMAProperties.FACING)).equals(neighbor)) {
            TileEntity te;
            if(world instanceof ChunkCache) {
                ChunkCache cache = (ChunkCache) world;
                te = cache.getTileEntity(pos);
            } else {
                te = world.getTileEntity(pos);
            }
            if(te instanceof PSITileEntity) {
                PSITileEntity pte = (PSITileEntity) te;
                pte.update();
            }
        }
        super.onNeighborChange(world, pos, neighbor);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity;

        if(world instanceof ChunkCache) {
            ChunkCache chunk = (ChunkCache) world;
            tileEntity = chunk.getTileEntity(pos);
        } else {
            tileEntity = world.getTileEntity(pos);
        }
        if(tileEntity instanceof PSITileEntity) {
            state = state.withProperty(MBMAProperties.CONNECTED, ((PSITileEntity) tileEntity).isConnected());
        }
        return state;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
                                            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getActualState(getDefaultState().withProperty(MBMAProperties.FACING, EnumFacing
                                                                                            .getDirectionFromEntityLiving(pos, placer)), world, pos);
    }
}
