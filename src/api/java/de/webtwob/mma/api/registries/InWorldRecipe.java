package de.webtwob.mma.api.registries;

import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

/**
 * Created by BB20101997 on 04. Dez. 2017.
 */
public abstract class InWorldRecipe extends IForgeRegistryEntry.Impl<InWorldRecipe> {
    
    public BlockPos getOffset(){
        return new BlockPos(0,0,0);
    }
    
    public abstract void spawResultAt(World world, BlockPos pos, EnumFacing facing);
    
    public abstract BlockPattern getBlockPattern();
    
    public abstract List<ItemStack> getResults(World world, BlockPos pos, EnumFacing facing);
    
    
}
