package de.webtwob.mma.api.crafting;

import com.google.common.base.Predicate;

import de.webtwob.mma.api.registries.InWorldRecipe;

import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

/**
 * Created by BB20101997 on 05. Dez. 2017.
 */
public class BasicInWorldRecipe extends InWorldRecipe {

    private BlockPattern          pattern;
    private Map<BlockPos, Object> resultMap;
    private Iterable<BlockPos>    clearBlocks;

    public BasicInWorldRecipe(String[][] layout, Map<Character, Predicate<BlockWorldState>> conditions, Map<BlockPos, Object> results) {
        resultMap = results;
        FactoryBlockPattern factory = FactoryBlockPattern.start();

        for (String[] aisle : layout) {
            factory.aisle(aisle);
        }

        for (Map.Entry<Character, Predicate<BlockWorldState>> entry : conditions.entrySet()) {
            factory.where(entry.getKey(), entry.getValue());
        }

        this.pattern = factory.build();

        int palmOffset   = Math.max(pattern.getPalmLength() - 1, 0);
        int thumbOffset  = Math.max(pattern.getThumbLength() - 1, 0);
        int fingerOffset = Math.max(pattern.getFingerLength() - 1, 0);
        clearBlocks = BlockPos.getAllInBox(0, 0, 0, palmOffset, thumbOffset, fingerOffset);
    }

    @Override
    public Map<BlockPos, Object> determineResult(World world, BlockPattern.PatternHelper patternHelper) {
        return resultMap;
    }

    @Override
    public void clearArea(World world, BlockPattern.PatternHelper patternHelper) {
        for (BlockPos pos : clearBlocks) {
            world.setBlockToAir(patternHelper.translateOffset(pos.getX(), pos.getY(), pos.getZ()).getPos());
        }
    }

    @Override
    public BlockPattern getBlockPattern() {
        return pattern;
    }

}
