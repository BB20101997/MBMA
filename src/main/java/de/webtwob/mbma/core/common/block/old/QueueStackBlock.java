package de.webtwob.mbma.core.common.block.old;

import de.webtwob.mbma.core.MBMA_CORE;
import de.webtwob.mbma.api.enums.MachineState;
import de.webtwob.mbma.api.interfaces.block.IDebugableBlock;
import de.webtwob.mbma.api.property.MBMAProperties;
import de.webtwob.mbma.core.common.MBMALog;
import de.webtwob.mbma.core.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.core.common.tileentity.old.TileEntityQueueOld;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mbma.api.enums.MachineState.IDLE;

/**
 * Created by bennet on 17.03.17.
 */
@Deprecated
public class QueueStackBlock extends Block implements IDebugableBlock {

    public QueueStackBlock() {
        super(Material.IRON);
        setCreativeTab(MBMACreativeTab.MBMATab);
        setDefaultState(blockState.getBaseState().withProperty(MBMAProperties.STATE, MachineState.PROBLEM));
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return getExtendedState(state, worldIn, pos);
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityQueueOld) {
            ((TileEntityQueueOld) te).destroyed();
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Nonnull
    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity;
        if (world instanceof ChunkCache) {
            ChunkCache chunkCache = (ChunkCache) world;
            tileEntity = chunkCache.getTileEntity(pos);
        } else {
            tileEntity = world.getTileEntity(pos);
        }
        if (tileEntity instanceof TileEntityQueueOld) {
            return state.withProperty(MBMAProperties.STATE, ((TileEntityQueueOld) tileEntity).getMachineState());
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(MBMAProperties.STATE) == IDLE ? 0 : 10;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (worldIn.isRemote) {
            return true;
        } else {
            if (playerIn.isSneaking() && playerIn.isCreative()) {
                TileEntity te = worldIn.getTileEntity(pos);
                if (te instanceof TileEntityQueueOld) {
                    MBMALog.info("{} creative completed a crafting request at {}", playerIn.getName(), pos);
                    //((TileEntityQueueOld) te).creativeComplete();
                }
            } else {
                playerIn.openGui(MBMA_CORE.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
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
        return new TileEntityQueueOld();
    }

    @Override
    public void performDebugOnBlock(World world, BlockPos pos, EntityPlayer player, int flag) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityQueueOld) {
            TileEntityQueueOld tileEntityQueue = (TileEntityQueueOld) tileEntity;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("State: ").append(tileEntityQueue.getMachineState());
            switch (tileEntityQueue.getMachineState()) {
                case IDLE:
                    break;
                case RUNNING:
                    stringBuilder.append('\n').append("Task: ").append(tileEntityQueue.getCraftingStatus());
                    break;
                case WAITING:
                    break;
                case PROBLEM:
                    stringBuilder.append('\n').append("Errors: ").append(tileEntityQueue.getErrorMessages());
                    break;
            }
            player.sendStatusMessage(new TextComponentString(stringBuilder.toString()), false);
        }
    }
}
