package de.webtwob.mbma.common.block;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.proxy.CommonProxy;
import de.webtwob.mbma.common.tileentity.TokenGeneratorTileEntity;
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
 * Created by bennet on 23.03.17.
 */
public class TokenGenerator extends Block{

    public TokenGenerator(){
        super(Material.IRON);
        setCreativeTab(MBMACreativeTab.MBMATab);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing dir, float x, float y, float z) {
        //open gui
        if(!world.isRemote){
            player.openGui(MultiblockMaschineAutomation.INSTANCE, CommonProxy.TOKEN_GENERATOR_GUI,world,blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TokenGeneratorTileEntity();
    }
}
