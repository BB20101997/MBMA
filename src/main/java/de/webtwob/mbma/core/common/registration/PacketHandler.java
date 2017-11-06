package de.webtwob.mbma.core.common.registration;

import de.webtwob.mbma.core.MBMA_CORE;
import de.webtwob.mbma.core.common.MBMALog;
import de.webtwob.mbma.core.common.packet.MaschineStateUpdatePacket;
import de.webtwob.mbma.core.common.packet.PageUpdatePacket;
import de.webtwob.mbma.core.common.packet.TokenUpdatePacket;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel
            (MBMA_CORE.MODID);

    private static int id = 0;
    private static boolean registered;
    
    private PacketHandler() {
    }
    
    public static int getNextID() {
        return id++;
    }

    public static void init() {
        if (!registered) {
            registered = true;
            MBMALog.debug("Registering PacketHandler");
            INSTANCE.registerMessage(MaschineStateUpdatePacket.MaschineStateUpdatePacketHandler.class, MaschineStateUpdatePacket.class, getNextID(), Side.CLIENT);
            INSTANCE.registerMessage(TokenUpdatePacket.TokenUpdatePacketHandler.class, TokenUpdatePacket.class, getNextID(), Side.SERVER);
            INSTANCE.registerMessage(PageUpdatePacket.PageUpdatePacketHandler.class, PageUpdatePacket.class, getNextID(), Side.SERVER);
            INSTANCE.registerMessage(PageUpdatePacket.PageUpdatePacketHandler.class, PageUpdatePacket.class, getNextID(), Side.CLIENT);
        }
    }
}
