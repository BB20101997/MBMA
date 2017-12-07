package de.webtwob.mma.core.common.registration.world.recipes;

import com.google.common.base.Predicate;
import de.webtwob.mma.api.crafting.BasicInWorldRecipe;
import de.webtwob.mma.api.util.MMAFilter;
import de.webtwob.mma.core.common.registration.Blocks;

import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BB20101997 on 06. Dez. 2017.
 */
public class DefaultRecipes {
    
    private static final String[][] CRAFTING_STORAGE_PATTERN = {{"BBB", "BBB", "BBB"}, {"BCB", "BWB", "BCB"}, {"BBB", "BBB", "BBB"}};
    private static final String[][] CRAFTING_PROCESSOR_PATTERN = {{"BBB", "BBB", "BBB"}, {"BAB", "BFB", "BWB"}, {"BBB", "BBB", "BBB"}};
    private static final Map<Character, Predicate<BlockWorldState>> RECIPE_PREDICATES = new HashMap<>();
    private static final Map<BlockPos, Object> CRAFTING_STORAGE_RESULT = new HashMap<>();
    private static final Map<BlockPos, Object> CRAFTING_PROCESSOR_RESULT = new HashMap<>();
    
    static {
        RECIPE_PREDICATES.put('A', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(net.minecraft.init.Blocks.ANVIL));
        RECIPE_PREDICATES.put('B', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(net.minecraft.init.Blocks.IRON_BARS));
        RECIPE_PREDICATES.put('C', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(net.minecraft.init.Blocks.CHEST));
        RECIPE_PREDICATES.put('F', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(net.minecraft.init.Blocks.FURNACE));
        RECIPE_PREDICATES.put('W', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(net.minecraft.init.Blocks.CRAFTING_TABLE));
        
        CRAFTING_STORAGE_RESULT.put(new BlockPos(1, 1, 1), Blocks.CRAFTING_STORAGE);
        CRAFTING_PROCESSOR_RESULT.put(new BlockPos(1, 1, 1), Blocks.CRAFTING_PROCESSOR);
    }
    
    public static final BasicInWorldRecipe CRAFTING_PROCESSOR_RECIPE = new BasicInWorldRecipe(CRAFTING_PROCESSOR_PATTERN, RECIPE_PREDICATES, CRAFTING_PROCESSOR_RESULT);
    public static final BasicInWorldRecipe CRAFTING_STORAGE_RECIPE = new BasicInWorldRecipe(CRAFTING_STORAGE_PATTERN, RECIPE_PREDICATES, CRAFTING_STORAGE_RESULT);
    
}
