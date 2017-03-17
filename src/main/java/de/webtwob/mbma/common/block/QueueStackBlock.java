package de.webtwob.mbma.common.block;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.references.UnlocalizedNames;
import de.webtwob.mbma.common.tileentity.QSTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by bennet on 17.03.17.
 */
public class QueueStackBlock extends Block{

    public QueueStackBlock() {
        super(Material.IRON);
        setCreativeTab(MBMACreativeTab.MBMATab);
        setUnlocalizedName(UnlocalizedNames.QUEUESTACK_NAME);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if(worldIn.isRemote){
            return true;
        }else{
            playerIn.openGui(MultiblockMaschineAutomation.INSTANCE,0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new QSTileEntity();
    }
}
