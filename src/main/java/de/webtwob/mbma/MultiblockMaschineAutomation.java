package de.webtwob.mbma;

import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.MBMAPacketHandler;
import de.webtwob.mbma.common.proxy.CommonProxy;
import de.webtwob.mbma.common.tileentity.PSITileEntity;
import de.webtwob.mbma.common.tileentity.QSTileEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber
@Mod(modid = MultiblockMaschineAutomation.MODID, useMetadata = true)
public class MultiblockMaschineAutomation {

    public static final String MODID      = "mbma";

    @Mod.Instance(MODID)
    public static MultiblockMaschineAutomation INSTANCE;

    @SidedProxy(clientSide = "de.webtwob.mbma.client.proxy.ClientProxy",serverSide = "de.webtwob.mbma.common.proxy.CommonProxy" )
    public static CommonProxy proxy;

    public MultiblockMaschineAutomation(){

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MBMALog.info("Starting PreInit!");
        GameRegistry.registerTileEntity(PSITileEntity.class, "mbma:psi");
        GameRegistry.registerTileEntity(QSTileEntity.class,"mbma:queuestack");
        MBMALog.info("Finished PreInit!");
    }

    @Mod.EventHandler
    public void inti(FMLInitializationEvent event) {
        MBMALog.info("Starting Init!");
        MBMAPacketHandler.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE,proxy);
        MBMALog.info("Finished Init!");
    }

}
