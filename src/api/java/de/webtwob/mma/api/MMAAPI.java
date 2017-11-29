package de.webtwob.mma.api;

import de.webtwob.mma.api.capability.APICapabilities;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by BB20101997 on 26. Okt. 2017.
 */

@Mod.EventBusSubscriber
@Mod(modid = MMAAPI.MODID, useMetadata = true)
public class MMAAPI {
    public static final String MODID = "mmaapi";

    @Mod.Instance(MODID)
    public static MMAAPI INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        APILog.info("Starting PreInit!");
        APICapabilities.register();
        /*
         * other stuff that would go here:
         * Ore-Dict assignment
         */
        APILog.info("Finished PreInit!");
    }
}
