package de.webtwob.mma.core.common.packet;

import io.netty.buffer.ByteBuf;

import de.webtwob.mma.api.enums.MachineState;
import de.webtwob.mma.api.interfaces.block.IMachineState;
import de.webtwob.mma.core.common.CoreLog;

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
public class MachineStateUpdatePacket implements IMessage {

    private MachineState machineState = MachineState.IDLE;
    private BlockPos     blockPos     = new BlockPos(0, 0, 0);

    /**
     * Used when generating Packets from the ByteStream from the Network
     */
    @SuppressWarnings("unused")
    public MachineStateUpdatePacket() {
    }

    /**
     * @param pos   the position of the Machine to update
     * @param state the state to set the Machine to
     */
    public MachineStateUpdatePacket(BlockPos pos, MachineState state) {
        blockPos = pos;
        machineState = state;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        machineState = MachineState.values()[buf.readInt()];
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(machineState.ordinal());
        if (blockPos != null) {
            buf.writeInt(blockPos.getX());
            buf.writeInt(blockPos.getY());
            buf.writeInt(blockPos.getZ());
        } else {
            for (int i = 0; i < 3; i++) {
                buf.writeInt(0);
            }
        }
    }

    public static class MachineStateUpdatePacketHandler implements IMessageHandler<MachineStateUpdatePacket, IMessage> {

        @Override
        public IMessage onMessage(MachineStateUpdatePacket message, MessageContext ctx) {
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT && message.blockPos != null) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
                    if (playerSP == null) {
                        return;
                    }
                    TileEntity tileEntity = playerSP.getEntityWorld().getTileEntity(message.blockPos);
                    if (tileEntity instanceof IMachineState) {
                        ((IMachineState) tileEntity).setMachineState(message.machineState);
                        tileEntity.markDirty();
                    } else {
                        CoreLog.warn(
                                "Received MachineStateUpdatePacket for Block at {} {} {}, but TileEntity " + "didn't support it!",
                                message.blockPos.getX(), message.blockPos.getY(), message.blockPos.getZ()
                        );
                    }
                });
            }
            return null;
        }
    }
}
