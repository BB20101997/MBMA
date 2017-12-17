package de.webtwob.mma.core.common.registration;

import com.google.gson.JsonParseException;
import org.apache.commons.io.FilenameUtils;

import de.webtwob.mma.api.APILog;
import de.webtwob.mma.api.registries.InWorldRecipe;
import de.webtwob.mma.api.util.InWorldRecipeLoader;
import de.webtwob.mma.core.MMACore;
import de.webtwob.mma.core.common.CoreLog;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Created by BB20101997 on 04. Dez. 2017.
 */
@Mod.EventBusSubscriber(modid = MMACore.MODID)
public class InWorldCraftingRecipes {

    private static final String RECIPE_DIR = "/assets/mmacore/3d_recipes";

    private InWorldCraftingRecipes() {
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<InWorldRecipe> event) {
        CoreLog.info("Registering InWorldRecipes");
        loadBuildInRecipes(event.getRegistry());
    }

    private static void loadBuildInRecipes(IForgeRegistry<InWorldRecipe> registry) {
        Stream<Path> pathIterator = null;
        FileSystem   fileSystem   = null;
        try {
            URI folderURI = InWorldRecipeLoader.class.getResource(RECIPE_DIR).toURI();
            Path folderPath;
            if ("jar".equals(folderURI.getScheme())) {
                fileSystem = FileSystems.newFileSystem(folderURI, Collections.emptyMap());
                folderPath = fileSystem.getPath(RECIPE_DIR);
            } else {
                folderPath = Paths.get(folderURI);
            }
            pathIterator = Files.walk(folderPath);
            for (Iterator<Path> it = pathIterator.iterator(); it.hasNext(); ) {
                Path path = it.next();
                if ("3d_recipe".equals(FilenameUtils.getExtension(path.toString()))) {
                    tryLoadingRecipe(registry, folderPath, path);
                }
            }
        } catch (IOException | URISyntaxException e) {
            APILog.LOGGER.error(e);
        } finally {
            if (fileSystem != null) {
                try {
                    fileSystem.close();
                } catch (IOException e) {
                    CoreLog.LOGGER.error(e);
                }
            }
            if (pathIterator != null) {
                pathIterator.close();
            }
        }

    }

    private static void tryLoadingRecipe(
            final IForgeRegistry<InWorldRecipe> registry, final Path folderPath, final Path path
    ) {
        Path relativePath = folderPath.relativize(path);
        ResourceLocation recipeRL = new ResourceLocation("mmacore",
                                                         FilenameUtils.removeExtension(relativePath.toString())
                                                                      .replaceAll("\\\\", "/")
        );
        try {
            InWorldRecipe recipe = InWorldRecipeLoader.loadRecipeFromReader(Files.newBufferedReader(path));
            recipe.setRegistryName(recipeRL);
            registry.register(recipe);
        } catch (NullPointerException | JsonParseException | ClassCastException | IllegalStateException e) {
            CoreLog.LOGGER.error("Failed to register build in InWorldRecipe " + recipeRL + " in " + path, e);
        } catch (Exception e) {
            CoreLog.LOGGER.error(
                    "Unexpected Exception while loading build in InWorldRecipe " + recipeRL + " in " + path, e);
        }
    }
}
