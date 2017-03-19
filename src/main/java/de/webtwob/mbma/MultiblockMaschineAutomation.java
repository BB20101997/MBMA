package de.webtwob.mbma;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.MBMAPacketHandler;
import de.webtwob.mbma.common.proxy.CommonProxy;
import de.webtwob.mbma.common.tileentity.TileEntityList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber
@Mod(modid = MultiblockMaschineAutomation.MODID, useMetadata = true)
public class MultiblockMaschineAutomation {

    public static final String MODID = "mbma";

    @Mod.Instance(MODID)
    public static MultiblockMaschineAutomation INSTANCE;

    @SidedProxy(clientSide = "de.webtwob.mbma.client.proxy.ClientProxy", serverSide = "de.webtwob.mbma.common.proxy"
                                                                                              + ".CommonProxy")
    public static CommonProxy proxy;

    public MultiblockMaschineAutomation() {
        MBMALog.info("ModInstance created!");
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MBMALog.info("Starting PreInit!");
        TileEntityList.registerTileEntities();
        APICapabilities.register();
        MBMALog.info("Finished PreInit!");
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void inti(FMLInitializationEvent event) {
        MBMALog.info("Starting Init!");
        MBMAPacketHandler.init();
        proxy.register();
        MBMALog.info("Finished Init!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //nothing to do at the moment
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        //register commands when existent
    }

    @Mod.EventHandler
    public void imc(FMLInterModComms.IMCEvent event) {
        for(FMLInterModComms.IMCMessage message : event.getMessages()) {
            MBMALog.warn("Received unexpected IMCMessage from ", message.getSender());
        }
    }

}
