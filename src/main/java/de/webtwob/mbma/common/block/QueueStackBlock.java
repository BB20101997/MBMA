package de.webtwob.mbma.common.block;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.api.MBMAProperties;
import de.webtwob.mbma.api.enums.MaschineState;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.tileentity.QSTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by bennet on 17.03.17.
 */
public class QueueStackBlock extends Block {

    public QueueStackBlock() {
        super(Material.IRON);
        setCreativeTab(MBMACreativeTab.MBMATab);

        setDefaultState(blockState.getBaseState().withProperty(MBMAProperties.STATE, MaschineState.IDLE));
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return getExtendedState(state, worldIn,pos);
    }

    @Nonnull
    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
      TileEntity tileEntity;
      if(world instanceof ChunkCache){
          ChunkCache chunkCache = (ChunkCache) world;
          tileEntity = chunkCache.getTileEntity(pos);
      }
      else{
          tileEntity = world.getTileEntity(pos);
      }
      if(tileEntity instanceof QSTileEntity){
          return state.withProperty(MBMAProperties.STATE,((QSTileEntity) tileEntity).getMaschineState());
      }
      return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(worldIn.isRemote) {
            return true;
        } else {
            playerIn.openGui(MultiblockMaschineAutomation.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MBMAProperties.STATE);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new QSTileEntity();
    }
}
