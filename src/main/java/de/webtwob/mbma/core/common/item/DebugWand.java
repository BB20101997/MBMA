package de.webtwob.mbma.core.common.item;

import de.webtwob.mbma.api.interfaces.block.IDebugableBlock;
import de.webtwob.mbma.api.interfaces.tileentity.IDebugableTile;
import de.webtwob.mbma.core.common.creativetab.MBMACreativeTab;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 17. Okt. 2017.
 */
public class DebugWand extends Item {
    
    public DebugWand() {
        super();
        setCreativeTab(MBMACreativeTab.MBMATab);
    }
    
    
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
                boolean debugable = false;
                IBlockState blockState = worldIn.getBlockState(result.getBlockPos());
                Block block = blockState.getBlock();
                
                if (block instanceof IDebugableBlock) {
                    ((IDebugableBlock) block).performDebugOnBlock(worldIn, result.getBlockPos(), playerIn, 0);
                    debugable = true;
                }
                
                TileEntity tileEntity;
                
                if (block.hasTileEntity(blockState) && (tileEntity = worldIn.getTileEntity(result.getBlockPos())) instanceof IDebugableTile) {
                    ((IDebugableTile) tileEntity).performDebugOnTile(playerIn);
                    debugable = true;
                }
                
                if (!debugable) {
                    playerIn.sendStatusMessage(new TextComponentString("No Debug Action available for " + block.getLocalizedName()), false);
                }
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
