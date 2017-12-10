package de.webtwob.mma.api.util;

import com.google.common.base.Predicate;
import com.google.gson.*;
import de.webtwob.mma.api.APILog;
import de.webtwob.mma.api.crafting.BasicInWorldRecipe;
import de.webtwob.mma.api.registries.InWorldRecipe;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.io.Files.newReader;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
@Mod.EventBusSubscriber
public class InWorldRecipeLoader {
    
    private static File location;
    
    private static void loadCustomRecipes(IForgeRegistry<InWorldRecipe> registry) {
        if (location != null) {
            //noinspection ResultOfMethodCallIgnored
            if (location.mkdirs() || location.exists())
                for (File file : FileUtils.listFiles(location, new String[]{"3drecipe"}, true)) {
                    
                    String s = FilenameUtils.removeExtension(location.toURI().relativize(file.toURI()).toString());
                    String[] domainSplit = s.split("/", 2);
                    
                    if (domainSplit.length == 2) {
                        ResourceLocation recipeRL = new ResourceLocation(domainSplit[0], domainSplit[1]);
                        try {
                            InWorldRecipe recipe = loadRecipeFromFile(file);
                            recipe.setRegistryName(recipeRL);
                            registry.register(recipe);
                        } catch (IOException exception) {
                            APILog.LOGGER.error("Couldn't read custom InWorldRecipe " + recipeRL + " from " + file, exception);
                        }
                    }
                    
                }
        }
    }
    
    public static void setLocation(File location) {
        InWorldRecipeLoader.location = location;
    }
    
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load loadEvent) {
        InWorldRecipeLoader.setLocation(new File(new File(DimensionManager.getCurrentSaveRootDirectory(), "data"), "3d_recipes"));
        IForgeRegistry<InWorldRecipe> registry = GameRegistry.findRegistry(InWorldRecipe.class);
        if (registry != null) {
            //loadCustomRecipes(registry); TODO fix crash when recipes are loaded after freeze
        }
    }
    
    @Nonnull
    public static InWorldRecipe loadRecipeFromFile(File recipeFile) throws FileNotFoundException {
    return loadRecipeFromReader(newReader(recipeFile,StandardCharsets.UTF_8));
    }
    
    public static InWorldRecipe loadRecipeFromReader(Reader recipeReader){
        Gson gson = new GsonBuilder().registerTypeAdapter(InWorldRecipe.class, new InWordRecipeDeserializer()).create();
        return gson.fromJson(recipeReader, InWorldRecipe.class);
    
    }
    
    private static class InWordRecipeDeserializer implements JsonDeserializer<InWorldRecipe> {
        
        @Override
        public InWorldRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject root = json.getAsJsonObject();
            String type = root.get("Type").getAsString();
            switch (type) {
                case "BlocksOnly": {
                    try {
                        return deserializeBlocksOnly(root);
                    } catch (IllegalStateException se) {
                        throw new JsonParseException(se);
                    }
                }
                default:
                    throw new JsonParseException("Unsupported Type:" + type);
            }
        }
        
        private BasicInWorldRecipe deserializeBlocksOnly(JsonObject root) {
            String[][] aisles = deserializeAisles(root.getAsJsonArray("Aisles"));
            Map<BlockPos, Object> resultMap = deserializeResultMap(root.getAsJsonArray("resultMap"));
            Map<Character, Predicate<BlockWorldState>> patternPredicates = deserializeBlockPredicates(root.getAsJsonArray("blockMap"));
            return new BasicInWorldRecipe(aisles, patternPredicates, resultMap);
        }
        
        private Map<Character, Predicate<BlockWorldState>> deserializeBlockPredicates(JsonArray blockMap) {
            Map<Character, Predicate<BlockWorldState>> result = new HashMap<>();
            for (JsonElement element : blockMap) {
                JsonObject mapElement = element.getAsJsonObject();
                char key = mapElement.get("key").getAsCharacter();
                String blockRL = mapElement.get("value").getAsString();
                result.put(key, (Predicate<BlockWorldState>) MMAFilter.areBlocksEqual(Block.getBlockFromName(blockRL)));
            }
            return result;
        }
        
        private Map<BlockPos, Object> deserializeResultMap(JsonArray resultMap) {
            Map<BlockPos, Object> result = new HashMap<>();
            for (JsonElement mapElement : resultMap) {
                JsonObject mapObject = mapElement.getAsJsonObject();
                Block resultBlock = Block.getBlockFromName(mapObject.get("blockName").getAsString());
                BlockPos pos = deserializeBlockPos(mapObject.get("pos").getAsJsonArray());
                result.put(pos, resultBlock);
            }
            return result;
        }
        
        private BlockPos deserializeBlockPos(JsonArray pos) {
            return new BlockPos(pos.get(0).getAsInt(), pos.get(1).getAsInt(), pos.get(2).getAsInt());
        }
        
        private String[][] deserializeAisles(JsonArray jsonAisles) {
            String[][] aisles = new String[jsonAisles.size()][];
            for (int i = 0; i < jsonAisles.size(); i++) {
                JsonArray jsonAisle = jsonAisles.get(i).getAsJsonArray();
                String[] aisle = new String[jsonAisle.size()];
                for (int j = 0; j < jsonAisle.size(); j++) {
                    aisle[j] = jsonAisle.get(j).getAsString();
                }
                aisles[i] = aisle;
            }
            return aisles;
        }
    }
}
