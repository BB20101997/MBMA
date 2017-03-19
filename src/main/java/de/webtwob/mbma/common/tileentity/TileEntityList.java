package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.common.MBMALog;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class TileEntityList {

    public static void registerTileEntities() {
        MBMALog.debug("Registering TileEntities");
        GameRegistry.registerTileEntity(PSITileEntity.class, "mbma:psi");
        GameRegistry.registerTileEntity(QSTileEntity.class, "mbma:queuestack");
    }
}
