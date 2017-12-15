package de.webtwob.mma.core.common.block;

import de.webtwob.mma.api.property.MMAProperties;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class BlockStorageInterface extends MMABlock {

    public BlockStorageInterface(ResourceLocation rl) {
        super(Material.IRON, rl);

        IBlockState state = blockState.getBaseState();
        state = state.withProperty(MMAProperties.FACING, EnumFacing.UP);
        state = state.withProperty(MMAProperties.CONNECTED, false);

        setDefaultState(state);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(MMAProperties.CONNECTED) ? 10 : 0;
    }

    /**
     * @deprecated
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {//NOSONAR
        return getDefaultState().withProperty(MMAProperties.FACING, EnumFacing.values()[meta >> 1])
                                .withProperty(MMAProperties.CONNECTED, (meta & 1) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(MMAProperties.FACING).ordinal() << 1) | (state.getValue(
                MMAProperties.CONNECTED) ? 1 : 0);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, MMAProperties.FACING, MMAProperties.CONNECTED);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        IBlockState state = world.getBlockState(pos);
        state = state.withProperty(
                MMAProperties.CONNECTED, isConnected(world, pos, state.getValue(MMAProperties.FACING)));
        if (world instanceof World) {
            ((World) world).setBlockState(pos, state);
        }
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(
            @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing clickedFacing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand
    ) {
        IBlockState state       = getDefaultState();
        EnumFacing  blockFacing = EnumFacing.getDirectionFromEntityLiving(pos, placer);
        state = state.withProperty(MMAProperties.FACING, blockFacing);
        state = state.withProperty(MMAProperties.CONNECTED, isConnected(world, pos, blockFacing));
        return state;
    }

    /**
     * @param world       the world the block is in
     * @param pos         the position the block is in
     * @param orientation the direction the block is facing
     *
     * @return whether the block is connected to a block that has the Item_HANDLER_CAPABILITY
     */
    private boolean isConnected(IBlockAccess world, BlockPos pos, EnumFacing orientation) {
        BlockPos   dest = pos.offset(orientation);
        TileEntity te;

        if (world instanceof ChunkCache) {
            ChunkCache chunk = (ChunkCache) world;
            te = chunk.getTileEntity(dest);
        } else {
            te = world.getTileEntity(dest);
        }

        return te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, orientation.getOpposite());
    }
}
