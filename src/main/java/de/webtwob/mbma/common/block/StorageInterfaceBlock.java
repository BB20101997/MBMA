package de.webtwob.mbma.common.block;

import de.webtwob.mbma.api.MBMAProperties;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class StorageInterfaceBlock extends Block {

    public StorageInterfaceBlock() {
        super(Material.IRON);
        setCreativeTab(MBMACreativeTab.MBMATab);

        IBlockState state = blockState.getBaseState();
        state = state.withProperty(MBMAProperties.FACING, EnumFacing.UP);
        state = state.withProperty(MBMAProperties.CONNECTED, false);

        setDefaultState(state);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(MBMAProperties.FACING, EnumFacing.values()[meta >> 1]).withProperty(MBMAProperties.CONNECTED, !((meta & 1) == 0));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(MBMAProperties.FACING).ordinal() << 1) | (state.getValue(MBMAProperties.CONNECTED) ? 1 : 0);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MBMAProperties.FACING, MBMAProperties.CONNECTED);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        IBlockState state = world.getBlockState(pos);
        state = state.withProperty(MBMAProperties.CONNECTED, connected(world, pos, state.getValue(MBMAProperties.FACING)));
        if(world instanceof World){
            ((World) world).setBlockState(pos,state);
        }
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing clickedFacing, float hitX, float hitY,
                                            float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        IBlockState state = getDefaultState();
        EnumFacing blockFacing = EnumFacing.getDirectionFromEntityLiving(pos, placer);
        state = state.withProperty(MBMAProperties.FACING, blockFacing);
        state = state.withProperty(MBMAProperties.CONNECTED, connected(world, pos, blockFacing));
        return state;
    }

    /**
     * @param world       the world the block is in
     * @param pos         the position the block is in
     * @param orientation the direction the block is facing
     * @return whether the block is connected to a block that has the Item_HANDLER_CAPABILITY
     */
    private boolean connected(IBlockAccess world, BlockPos pos, EnumFacing orientation) {
        BlockPos dest = pos.offset(orientation);
        TileEntity te;
        if (world instanceof ChunkCache) {
            ChunkCache chunk = (ChunkCache) world;
            te = chunk.getTileEntity(pos);
        } else {
            te = world.getTileEntity(dest);
        }

        return te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, orientation.getOpposite());
    }
}
