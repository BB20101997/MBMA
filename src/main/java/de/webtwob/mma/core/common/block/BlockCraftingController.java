package de.webtwob.mma.core.common.block;

import de.webtwob.mma.api.enums.MachineState;
import de.webtwob.mma.api.interfaces.tileentity.IMultiBlockTile;
import de.webtwob.mma.api.inventory.ApiCommonProxy;
import de.webtwob.mma.api.property.MMAProperties;
import de.webtwob.mma.core.common.references.BlockHolder;
import de.webtwob.mma.core.common.tileentity.TileEntityCraftingController;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 28. Okt. 2017.
 */
public class BlockCraftingController extends MMABlock{
    
    public BlockCraftingController(ResourceLocation rl) {
        super(Material.IRON,rl);
        
        IBlockState state = blockState.getBaseState();
        state = state.withProperty(MMAProperties.STATE, MachineState.IDLE);
        setDefaultState(state);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
    
    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MMAProperties.STATE);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(BlockHolder.apiInstance, ApiCommonProxy.TILE_ENTITY_GUI,world,pos.getX(),pos.getY(),pos.getZ());
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
        return new TileEntityCraftingController();
    }
    
    /**
     * @deprecated
     */
    @Nonnull
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {//NOSONAR
        return getExtendedState(state, worldIn, pos);
    }
    
    @Nonnull
    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCraftingController) {
            return state.withProperty(MMAProperties.STATE, ((TileEntityCraftingController) tileEntity).getState());
        }
        return state.withProperty(MMAProperties.STATE, MachineState.PROBLEM);
    }
    
    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        
        if (tileEntity instanceof IMultiBlockTile) {
            ((IMultiBlockTile) tileEntity).onBlockBreak(worldIn, pos);
        }
        
        super.breakBlock(worldIn, pos, state);
    }
}
