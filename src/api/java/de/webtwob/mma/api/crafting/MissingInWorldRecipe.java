package de.webtwob.mma.api.crafting;

import de.webtwob.mma.api.registries.InWorldRecipe;

import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Map;

/**
 * Created by BB20101997 on 17. Dez. 2017.
 */
public class MissingInWorldRecipe extends InWorldRecipe {

    private static final Map<BlockPos, Object> MISSING_RESULT = Collections.emptyMap();
    private static final BlockPattern NEVER_MATCH;

    static {
        NEVER_MATCH = FactoryBlockPattern.start().aisle("F").where('F', e -> false).build();
    }

    /**
     * @param networked is necessary as it makes the difference between a DummyFactory and a MissingFactory!
     * */
    public MissingInWorldRecipe(ResourceLocation key, @SuppressWarnings("unused") boolean networked) {
        setRegistryName(key);
    }

    @Override
    public Map<BlockPos, Object> determineResult(final World world, final BlockPattern.PatternHelper patternHelper) {
        return MISSING_RESULT;
    }

    @Override
    public void clearArea(final World world, final BlockPattern.PatternHelper patternHelper) {
        //NO-OP
    }

    @Override
    public BlockPattern getBlockPattern() {
        return NEVER_MATCH;
    }
}
