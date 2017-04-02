package de.webtwob.mbma.common.block;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.proxy.CommonProxy;
import de.webtwob.mbma.common.tileentity.RecipeBankTileEntity;
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
 * Created by bennet on 01.04.17.
 */
public class RecipeBank extends Block {
    public RecipeBank() {
        super(Material.IRON);
        setCreativeTab(MBMACreativeTab.MBMATab);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_) {
        if(!world.isRemote){
            player.openGui(MultiblockMaschineAutomation.INSTANCE, CommonProxy.RECIPE_BANK_GUI,world,pos.getX(),pos.getY(),pos.getZ());
        }
        return true;
    }
    
    @Override
    public boolean hasTileEntity(final IBlockState p_hasTileEntity_1_) {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(
            final World p_createTileEntity_1_, final IBlockState p_createTileEntity_2_
    ) {
        return new RecipeBankTileEntity();
    }
}
