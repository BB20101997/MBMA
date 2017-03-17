package de.webtwob.mbma;

import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.MBMAPacketHandler;
import de.webtwob.mbma.common.tileentity.PSITileEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber
@Mod(modid = MultiblockMaschineAutomation.MODID, useMetadata = true)
public class MultiblockMaschineAutomation {

    public static final String MODID      = "mbma";

    public MultiblockMaschineAutomation(){

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MBMALog.info("Starting PreInit!");
        GameRegistry.registerTileEntity(PSITileEntity.class, "mbma:psi");
        MBMALog.info("Finished PreInit!");
    }

    @Mod.EventHandler
    public void inti(FMLInitializationEvent event) {
        MBMALog.info("Starting Init!");
        MBMAPacketHandler.init();
        MBMALog.info("Finished Init!");
    }

}
