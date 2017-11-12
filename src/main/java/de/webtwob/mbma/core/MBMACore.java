package de.webtwob.mbma.core;

import de.webtwob.mbma.core.common.CoreLog;
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
@Mod(modid = MBMACore.MODID, useMetadata = true)
public class MBMACore {
    
    public static final String MODID = "mbmacore";
    public static final String COMMON_PROXY = "de.webtwob.mbma.core.common.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "de.webtwob.mbma.core.client.proxy.ClientProxy";
    
    @Mod.Instance(MODID)
    public static MBMACore INSTANCE = null;
    
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy PROXY = null;
    
    public MBMACore() {
        CoreLog.info("ModInstance created!");
    }
    
    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CoreLog.info("Starting PreInit!");
        Tiles.registerTileEntities();
        /*
         * other stuff that would go here:
         * Ore-Dict assignment
         */
        CoreLog.info("Finished PreInit!");
    }
    
    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void inti(FMLInitializationEvent event) {
        CoreLog.info("Starting Init!");
        PacketHandler.init();
        PROXY.register();
        /*
         * other stuff that would go here:
         * Register WorldGenerators
         * Register EventHandler
         */
        CoreLog.info("Finished Init!");
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        //cross-mod-stuff
        CoreLog.info("Nothing to do in PostInit!");
    }
    
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        //register commands when existent
        CoreLog.info("Nothing to do on ServerStart!");
    }
    
    @Mod.EventHandler
    public void imc(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            CoreLog.warn("Received unexpected IMCMessage from ", message.getSender());
        }
    }
    
}
