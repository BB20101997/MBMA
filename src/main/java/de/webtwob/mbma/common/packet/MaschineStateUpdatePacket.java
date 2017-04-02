package de.webtwob.mbma.common.packet;

import de.webtwob.mbma.api.enums.MaschineState;
import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.api.interfaces.IMaschineState;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class MaschineStateUpdatePacket implements IMessage {

    private MaschineState maschineState = MaschineState.IDLE;
    private BlockPos      blockPos      = new BlockPos(0, 0, 0);

    public MaschineStateUpdatePacket() {}

    public MaschineStateUpdatePacket(BlockPos pos, MaschineState state) {
        blockPos = pos;
        maschineState = state;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        maschineState = MaschineState.values()[buf.readInt()];
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(maschineState.ordinal());
        if(blockPos != null) {
            buf.writeInt(blockPos.getX());
            buf.writeInt(blockPos.getY());
            buf.writeInt(blockPos.getZ());
        } else {
            for(int i = 0; i < 3; i++) { buf.writeInt(0); }
        }
    }

    public static class MaschineStateUpdatePacketHandler implements IMessageHandler<MaschineStateUpdatePacket,
                                                                                           IMessage> {

        @Override
        public IMessage onMessage(MaschineStateUpdatePacket message, MessageContext ctx) {
            if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                if(message.blockPos != null) {
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
                        if(playerSP == null) { return; }
                        TileEntity tileEntity = playerSP.getEntityWorld().getTileEntity(message.blockPos);
                        if(tileEntity instanceof IMaschineState) {
                            ((IMaschineState) tileEntity).setMaschineState(message.maschineState);
                            tileEntity.markDirty();
                        } else {
                            MBMALog.warn("Received MaschineStateUpdatePacket for Block at {} {} {}, but TileEntity " +
                                                 "didn't support it!", message.blockPos.getX(), message.blockPos.getY(), message.blockPos.getZ());
                        }
                    });
                }
            }
            return null;
        }
    }
}
