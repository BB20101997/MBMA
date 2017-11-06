package de.webtwob.mbma.core;

import de.webtwob.mbma.core.common.MBMALog;
import de.webtwob.mbma.core.common.proxy.CommonProxy;
import de.webtwob.mbma.core.common.registration.PacketHandler;
import de.webtwob.mbma.core.common.registration.Tiles;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber
@Mod(modid = MBMA_CORE.MODID, useMetadata = true)
public class MBMA_CORE {
    
    public static final String MODID = "mbmacore";
    public static final String COMMON_PROXY = "de.webtwob.mbma.core.common.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "de.webtwob.mbma.core.client.proxy.ClientProxy";
    
    @Mod.Instance(MODID)
    public static MBMA_CORE INSTANCE;
    
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;
    
    public MBMA_CORE() {
        MBMALog.info("ModInstance created!");
    }
    
    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MBMALog.info("Starting PreInit!");
        Tiles.registerTileEntities();
        /*
         * other stuff that would go here:
         * Ore-Dict assignment
         */
        MBMALog.info("Finished PreInit!");
    }
    
    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void inti(FMLInitializationEvent event) {
        MBMALog.info("Starting Init!");
        PacketHandler.init();
        proxy.register();
        /*
         * other stuff that would go here:
         * Register WorldGenerators
         * Register EventHandler
         */
        MBMALog.info("Finished Init!");
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //cross-mod-stuff
        MBMALog.info("Nothing to do in PostInit!");
    }
    
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        //register commands when existent
        MBMALog.info("Nothing to do on ServerStart!");
    }
    
    @Mod.EventHandler
    public void imc(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            MBMALog.warn("Received unexpected IMCMessage from ", message.getSender());
        }
    }
    
}
