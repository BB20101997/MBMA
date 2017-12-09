package de.webtwob.mma.core.common.registration.world.recipes;

import com.google.common.base.Predicate;

import de.webtwob.mma.api.crafting.BasicInWorldRecipe;
import de.webtwob.mma.api.util.MMAFilter;
import de.webtwob.mma.core.common.registration.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 06. Dez. 2017.
 */
@SuppressWarnings("WeakerAccess")
public class DefaultRecipes {
    
    @ObjectHolder("minecraft:anvil")
    public static final Block ANVIL          = null;
    @ObjectHolder("minecraft:iron_bars")
    public static final Block IRON_BARS      = null;
    @ObjectHolder("minecraft:chest")
    public static final Block CHEST          = null;
    @ObjectHolder("minecraft:furnace")
    public static final Block FURNACE        = null;
    @ObjectHolder("minecraft:crafting_table")
    public static final Block CRAFTING_TABLE = null;
    
    public static final BasicInWorldRecipe CRAFTING_PROCESSOR_RECIPE;
    public static final BasicInWorldRecipe CRAFTING_STORAGE_RECIPE;
    
    private static final String[] BAR_WALL = {"BBB", "BBB", "BBB"};
    
    private static final String[][] CRAFTING_STORAGE_PATTERN   = {BAR_WALL, {"BCB", "BWB", "BCB"}, BAR_WALL};
    private static final String[][] CRAFTING_PROCESSOR_PATTERN = {BAR_WALL, {"BAB", "BFB", "BWB"}, BAR_WALL};
    
    private static final Map<Character, Predicate<BlockWorldState>> RECIPE_PREDICATES         = new HashMap<>();
    private static final Map<BlockPos, Object>                      CRAFTING_STORAGE_RESULT   = new HashMap<>();
    private static final Map<BlockPos, Object>                      CRAFTING_PROCESSOR_RESULT = new HashMap<>();
    
    static {
        RECIPE_PREDICATES.put('A', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(ANVIL));
        RECIPE_PREDICATES.put('B', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(IRON_BARS));
        RECIPE_PREDICATES.put('C', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(CHEST));
        RECIPE_PREDICATES.put('F', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(FURNACE));
        RECIPE_PREDICATES.put('W', (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(CRAFTING_TABLE));
        
        CRAFTING_STORAGE_RESULT.put(new BlockPos(1, 1, 1), Blocks.CRAFTING_STORAGE);
        CRAFTING_PROCESSOR_RESULT.put(new BlockPos(1, 1, 1), Blocks.CRAFTING_PROCESSOR);
        
        CRAFTING_PROCESSOR_RECIPE = new BasicInWorldRecipe(
                CRAFTING_PROCESSOR_PATTERN, RECIPE_PREDICATES, CRAFTING_PROCESSOR_RESULT);
        CRAFTING_STORAGE_RECIPE = new BasicInWorldRecipe(
                CRAFTING_STORAGE_PATTERN, RECIPE_PREDICATES, CRAFTING_STORAGE_RESULT);
    }
    
    private DefaultRecipes() {}
    
}
