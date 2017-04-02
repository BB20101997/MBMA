package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.references.MBMAResources;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class TileEntityList {

    public static void registerTileEntities() {
        MBMALog.debug("Registering TileEntities");
        GameRegistry.registerTileEntity(QSTileEntity.class, MBMAResources.QUEUESTACK_REGISTRY_NAME.toString());
        GameRegistry.registerTileEntity(TokenGeneratorTileEntity.class,MBMAResources.TOKEN_GENERATOR_REGISTRY_NAME.toString());
        GameRegistry.registerTileEntity(RecipeBankTileEntity.class, MBMAResources.RECIPE_BANK_REGISTRY_NAME.toString());
    }
}
