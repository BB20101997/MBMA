package de.webtwob.mbma.core.common.registration;

import de.webtwob.mbma.core.common.CoreLog;
import de.webtwob.mbma.core.common.tileentity.*;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static de.webtwob.mbma.core.common.references.ResourceLocations.Blocks.*;


/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmacore")
public class Tiles {

    private Tiles() {
    }

    public static void registerTileEntities() {
        CoreLog.debug("Registering TileEntities");
        GameRegistry.registerTileEntity(TileEntityStorageIndexer.class,STORAGE_INDEXER_RL.toString());

        GameRegistry.registerTileEntity(TileEntityRequestGenerator.class, TOKEN_GENERATOR_REGISTRY_NAME.toString());
        GameRegistry.registerTileEntity(TileEntityQueue.class, QUEUE_RL.toString());

        GameRegistry.registerTileEntity(TileEntityPatternStore.class,PATTERN_STORAGE_REGISTRY_NAME.toString());

        GameRegistry.registerTileEntity(TileEntityCraftingController.class,CRAFTING_CONTROLLER_RL.toString());
        GameRegistry.registerTileEntity(TileEntityCraftingProcessor.class, CRAFTING_PROCESSOR_RL.toString());
        GameRegistry.registerTileEntity(TileEntityCraftingStorage.class,CRAFTING_STORAGE_RL.toString());
    }

}

