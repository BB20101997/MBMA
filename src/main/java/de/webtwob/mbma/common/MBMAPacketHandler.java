package de.webtwob.mbma.common;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.common.packet.MaschineStateUpdatePacket;
import de.webtwob.mbma.common.packet.TokenUpdatePacket;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class MBMAPacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel
            (MultiblockMaschineAutomation.MODID);

    private static int id = 0;
    private static boolean registered;

    public static int getNextID(){
        return id++;
    }

    public static void init() {
        if (!registered) {
            registered = true;
            MBMALog.debug("Registering PacketHandler");
            INSTANCE.registerMessage(MaschineStateUpdatePacket.MaschineStateUpdatePacketHandler.class, MaschineStateUpdatePacket.class, getNextID(), Side.CLIENT);
            INSTANCE.registerMessage(TokenUpdatePacket.TokenUpdatePacketHandler.class, TokenUpdatePacket.class, getNextID(), Side.SERVER);
        }
    }
}
