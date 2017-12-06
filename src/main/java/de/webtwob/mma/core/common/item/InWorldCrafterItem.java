package de.webtwob.mma.core.common.item;

import de.webtwob.mma.api.registries.InWorldRecipe;
import de.webtwob.mma.core.common.creativetab.MMACreativeTab;

import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by BB20101997 on 04. Dez. 2017.
 */
public class InWorldCrafterItem extends Item {
    
    public InWorldCrafterItem(){
        setCreativeTab(MMACreativeTab.MMATab);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn!=null&&pos!=null&&facing!=null){
            if(!worldIn.isRemote){
                IForgeRegistry<InWorldRecipe> registry = GameRegistry.findRegistry(InWorldRecipe.class);
                if(registry!=null)
                    for (InWorldRecipe recipe:registry.getValues()){
                        BlockPattern pattern = recipe.getBlockPattern();
                        BlockPattern.PatternHelper helper = pattern.match(worldIn,pos.offset(facing.getOpposite()));
                        if(helper!=null){
                            recipe.executeRecipe(worldIn, helper);
                            return EnumActionResult.SUCCESS;
                        }
                    }
                    return EnumActionResult.PASS;
            }
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public boolean canItemEditBlocks() {
        return true;
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return true;
    }
}
