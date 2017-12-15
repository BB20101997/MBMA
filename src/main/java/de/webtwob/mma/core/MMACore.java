package de.webtwob.mma.core;

import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.registration.PacketHandler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber
@Mod(modid = MMACore.MODID, useMetadata = true)
public class MMACore {

    public static final String MODID = "mmacore";

    @Mod.Instance(MODID)
    public static MMACore INSTANCE = null;//NOSONAR

    public MMACore() {
        CoreLog.info("ModInstance created!");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CoreLog.info("Starting PreInit!");
        /*
         * other stuff that would go here:
         */
        CoreLog.info("Finished PreInit!");
    }

    @Mod.EventHandler
    public void inti(FMLInitializationEvent event) {
        CoreLog.info("Starting Init!");
        PacketHandler.init();
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
