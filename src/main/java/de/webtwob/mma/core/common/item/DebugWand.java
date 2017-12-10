package de.webtwob.mma.core.common.item;

import de.webtwob.mma.api.interfaces.block.IDebuggableBlock;
import de.webtwob.mma.api.interfaces.tileentity.IDebuggableTile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 17. Okt. 2017.
 */
public class DebugWand extends MMAItem {

    /**
     * The Constructor to create a new DebugWand Instance
     */
    public DebugWand(ResourceLocation rl) {
        super(rl);
    }


    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        RayTraceResult result = rayTrace(worldIn, playerIn, true);
        /*
         * My IDE tells me result is never null, I have had a NullPointerException because it was null!
         * rayTrace returns the result of a Nullable function w/o rayTrace being Nullable
         * * */
        //noinspection ConstantConditions
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            if (!worldIn.isRemote) {
                boolean debuggable;
                IBlockState blockState = worldIn.getBlockState(result.getBlockPos());
                Block block = blockState.getBlock();

                debuggable = debugBlock(block, worldIn, result.getBlockPos(), playerIn);
                debuggable |= debugTile(block, blockState, worldIn, result.getBlockPos(), playerIn);

                if (!debuggable) {
                    playerIn.sendStatusMessage(new TextComponentString("No Debug Action available for " + block.getLocalizedName()), false);
                }
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private boolean debugBlock(Block block, World world, BlockPos pos, EntityPlayer player) {
        if (block instanceof IDebuggableBlock) {
            player.sendStatusMessage(new TextComponentString("Debug Information for Block " + block.getLocalizedName()), false);
            ((IDebuggableBlock) block).performDebugOnBlock(world, pos, player, 0);
            return true;
        }
        return false;
    }

    private boolean debugTile(Block block, IBlockState state, World world, BlockPos pos, EntityPlayer player) {
        if (block.hasTileEntity(state)) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof IDebuggableTile) {
                player.sendStatusMessage(new TextComponentString("Debug Information for TileEntity of Block " + block.getLocalizedName()), false);
                ((IDebuggableTile) tileEntity).performDebugOnTile(player);
                return true;
            }
        }
        return false;
    }
}
