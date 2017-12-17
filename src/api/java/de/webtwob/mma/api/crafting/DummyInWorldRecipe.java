package de.webtwob.mma.api.crafting;

import de.webtwob.mma.api.registries.InWorldRecipe;

import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BB20101997 on 17. Dez. 2017.
 */
public class DummyInWorldRecipe extends InWorldRecipe {

    public DummyInWorldRecipe(ResourceLocation key){
        setRegistryName(key);
    }

    @Override
    public Map<BlockPos, Object> determinResult(World world, BlockPattern.PatternHelper patternHelper) {
        return new HashMap<>();
    }

    @Override
    public void clearArea(World world, BlockPattern.PatternHelper patternHelper) {
        //NO-OP this is a dummy
    }

    @Override
    public BlockPattern getBlockPattern() {
        //Always failing as this is a dummy
        return FactoryBlockPattern.start().aisle("A").where('A', o -> false).build();
    }
}
