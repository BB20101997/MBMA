package de.webtwob.mbma.common.block;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by bennet on 23.03.17.
 */
public class TokenGenerator extends Block{

    public TokenGenerator(){
        super(Material.IRON);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing dir, float p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_) {
        //open gui
        if(!world.isRemote){
            player.openGui(MultiblockMaschineAutomation.INSTANCE,2,world,blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
        return true;
    }
}
