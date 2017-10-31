package de.webtwob.mbma.core.common.registration;

import de.webtwob.mbma.core.common.MBMALog;
import de.webtwob.mbma.core.common.tileentity.TileEntityCraftingController;
import de.webtwob.mbma.core.common.tileentity.TileEntityCraftingProcessor;
import de.webtwob.mbma.core.common.tileentity.TileEntityCraftingStorage;
import de.webtwob.mbma.core.common.tileentity.TileEntityRequestGenerator;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.Blocks.*;


/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmacore")
public class MBMATileEntityList {
    
    private MBMATileEntityList() {
    }
    
    public static void registerTileEntities() {
        MBMALog.debug("Registering TileEntities");
        GameRegistry.registerTileEntity(TileEntityRequestGenerator.class, TOKEN_GENERATOR_REGISTRY_NAME.toString());
        GameRegistry.registerTileEntity(TileEntityCraftingController.class,CRAFTING_CONTROLLER_RL.toString());
        GameRegistry.registerTileEntity(TileEntityCraftingProcessor.class, CRAFTING_PROCESSOR_RL.toString());
        GameRegistry.registerTileEntity(TileEntityCraftingStorage.class,CRAFTING_STORAGE_RL.toString());
    }

}

