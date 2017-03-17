package de.webtwob.mbma.common.packet;

import de.webtwob.mbma.common.tileentity.PSITileEntity;
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
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class PSIBStatePacket implements IMessage {

    public PSIBStatePacket() {}

    BlockPos blockPos;
    boolean  connected;

    public PSIBStatePacket(BlockPos pos, boolean connected) {
        blockPos = pos;
        this.connected = connected;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        connected = buf.readBoolean();
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(connected);
        if(blockPos != null) {
            buf.writeInt(blockPos.getX());
            buf.writeInt(blockPos.getY());
            buf.writeInt(blockPos.getZ());
        } else {
            buf.writeInt(0);
            buf.writeInt(0);
            buf.writeInt(0);
        }
    }

    public static class PSIBStatePacketHandler implements IMessageHandler<PSIBStatePacket, IMessage> {

        @Override
        public IMessage onMessage(PSIBStatePacket message, MessageContext ctx) {
            if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                if(message.blockPos != null) {
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
                        if(playerSP==null)
                            return;
                        TileEntity tileEntity = playerSP.getEntityWorld().getTileEntity(message.blockPos);
                        if(tileEntity instanceof PSITileEntity) {
                            ((PSITileEntity)tileEntity).setConnected(message.connected);
                            tileEntity.markDirty();
                        }else{
                            System.out.println("Wrong TileEntity");
                        }
                    });
                }
            }
            return null;
        }
    }
}
