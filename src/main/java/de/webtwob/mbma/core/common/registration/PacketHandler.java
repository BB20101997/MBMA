package de.webtwob.mbma.core.common.registration;

import de.webtwob.mbma.core.MBMACore;
import de.webtwob.mbma.core.common.CoreLog;
import de.webtwob.mbma.core.common.packet.MachineStateUpdatePacket;
import de.webtwob.mbma.core.common.packet.TokenUpdatePacket;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel
            (MBMACore.MODID);

    private static int id = 0;
    private static boolean registered;

    private PacketHandler() {
    }

    public static int getNextID() {
        return id++;
    }

    /**
     * Initialises the PacketHandler by registering all Messages to the Channel
     */
    public static void init() {
        if (!registered) {
            registered = true;
            CoreLog.debug("Registering PacketHandler");
            INSTANCE.registerMessage(MachineStateUpdatePacket.MachineStateUpdatePacketHandler.class, MachineStateUpdatePacket.class, getNextID(), Side.CLIENT);
            INSTANCE.registerMessage(TokenUpdatePacket.TokenUpdatePacketHandler.class, TokenUpdatePacket.class, getNextID(), Side.SERVER);
        }
    }
}
