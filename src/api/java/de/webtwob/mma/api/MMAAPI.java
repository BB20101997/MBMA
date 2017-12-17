package de.webtwob.mma.api;

import de.webtwob.mma.api.capability.APICapabilities;
import de.webtwob.mma.api.inventory.ApiCommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by BB20101997 on 26. Okt. 2017.
 */

@Mod.EventBusSubscriber
@Mod(modid = MMAAPI.MODID, useMetadata = true)
public class MMAAPI {

    public static final String MODID        = "mmaapi";
    public static final String CLIENT_PROXY = "de.webtwob.mma.api.inventory.ApiClientProxy";
    public static final String COMMON_PROXY = "de.webtwob.mma.api.inventory.ApiCommonProxy";

    @Mod.Instance(MODID)
    public static MMAAPI INSTANCE;//NOSONAR

    @SidedProxy(serverSide = COMMON_PROXY, clientSide = CLIENT_PROXY)
    private static ApiCommonProxy proxy = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        APILog.info("Starting PreInit!");
        APICapabilities.register();
        APILog.info("Finished PreInit!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        APILog.info("Starting Init!");
        NetworkRegistry.INSTANCE.registerGuiHandler(MODID, proxy);
        APILog.info("Finished Init!");
    }
}
