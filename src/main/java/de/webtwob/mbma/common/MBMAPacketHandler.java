package de.webtwob.mbma.common;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.common.packet.MaschineStateUpdatePacket;
import de.webtwob.mbma.common.packet.PSIBStatePacket;
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

    static {
        MBMALog.debug("Registering PacketHandler");
        INSTANCE.registerMessage(PSIBStatePacket.PSIBStatePacketHandler.class, PSIBStatePacket.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MaschineStateUpdatePacket.MaschineStateUpdatePacketHandler.class,MaschineStateUpdatePacket.class,id++,Side.CLIENT);
    }

    public static void init() {/*Just here to make the static block run!*/}
}
