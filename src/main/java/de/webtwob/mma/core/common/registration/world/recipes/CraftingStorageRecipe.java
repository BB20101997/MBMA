package de.webtwob.mma.core.common.registration.world.recipes;

import de.webtwob.mma.api.registries.InWorldRecipe;
import de.webtwob.mma.api.util.MMAFilter;
import de.webtwob.mma.core.common.registration.Items;

import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 05. Dez. 2017.
 */
public class CraftingStorageRecipe extends InWorldRecipe {
    @Override
    public void spawResultAt(World world, BlockPos pos, EnumFacing facing) {
        switch (facing.getAxis()) {
            
            case X:
                setRageToAir(world, pos.add(0, -1, -1), pos.add(facing.getAxisDirection().getOffset() * -2, 1, 1));
                break;
            case Y:
                setRageToAir(world, pos.add(-1, 0, -1), pos.add(1, facing.getAxisDirection().getOffset() * -2, 1));
                break;
            case Z:
                setRageToAir(world, pos.add(-1, -1, 0), pos.add(1, 1, facing.getAxisDirection().getOffset() * -2));
                break;
        }
        world.setBlockState(pos.offset(facing.getOpposite()), de.webtwob.mma.core.common.registration.Blocks.PATTERN_STORAGE.getDefaultState());
    }
    
    @Override
    public BlockPattern getBlockPattern() {
        return FactoryBlockPattern.start()
                .aisle("BBB", "BBB", "BBB")
                .aisle("BBB", "CWC", "BBB")
                .aisle("BBB", "BBB", "BBB")
                .where('B', state -> MMAFilter.checkIfNotNull(state, o -> o.getBlockState().getBlock().equals(Blocks.IRON_BARS)))
                .where('C', state -> MMAFilter.checkIfNotNull(state, o -> o.getBlockState().getBlock().equals(Blocks.CHEST)))
                .where('W', state -> MMAFilter.checkIfNotNull(state, o -> o.getBlockState().getBlock().equals(Blocks.CRAFTING_TABLE)))
                .build();
    }
    
    @Override
    public List<ItemStack> getResults(World world, BlockPos pos, EnumFacing facing) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(Items.CRAFTING_STORAGE_ITEM));
        return list;
    }
    
    private void setRageToAir(World world, BlockPos pos1, BlockPos pos2) {
        for (BlockPos pos : BlockPos.getAllInBox(pos1, pos2)) {
            world.setBlockToAir(pos);
        }
    }
}
