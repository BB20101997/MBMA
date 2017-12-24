package de.webtwob.mma.api.registries;

import de.webtwob.mma.api.APILog;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Map;

/**
 * Created by BB20101997 on 04. Dez. 2017.
 */
public abstract class InWorldRecipe extends IForgeRegistryEntry.Impl<InWorldRecipe> {

    public final void executeRecipe(World world, BlockPattern.PatternHelper patternHelper) {
        Map<BlockPos, Object> resultMap = determineResult(world, patternHelper);
        clearArea(world, patternHelper);
        spawnResult(world, patternHelper, resultMap);
    }

    public abstract Map<BlockPos, Object> determineResult(World world, BlockPattern.PatternHelper patternHelper);

    public abstract void clearArea(World world, BlockPattern.PatternHelper patternHelper);

    public void spawnResult(World world, BlockPattern.PatternHelper patternHelper, Map<BlockPos, Object> resultMap) {
        for (Map.Entry<BlockPos, Object> entry : resultMap.entrySet()) {
            Object result = entry.getValue();
            BlockPos pos = patternHelper.translateOffset(entry.getKey().getX(), entry.getKey().getY(),
                                                         entry.getKey().getZ()
            ).getPos();

            if (result instanceof Block || result instanceof IBlockState) {
                IBlockState state;
                if (result instanceof Block) {
                    state = ((Block) result).getDefaultState();
                } else {
                    state = (IBlockState) result;
                }
                world.setBlockState(pos, state);
            } else if (result instanceof ItemStack || result instanceof Entity) {
                Entity entity;

                if (result instanceof ItemStack) {
                    EntityItem itemEntity = new EntityItem(world);
                    itemEntity.setItem((ItemStack) result);
                    entity = itemEntity;
                } else {
                    entity = (Entity) result;
                }

                entity.setWorld(world);
                entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                world.spawnEntity(entity);
            } else {
                APILog.error(
                        "ResultMap contained Object of unsupported class {} \n Only Block, IBlockState, ItemStack, Entity and sub-classes are supported!.",
                        result.getClass()
                );
            }
        }

    }

    public abstract BlockPattern getBlockPattern();

}
