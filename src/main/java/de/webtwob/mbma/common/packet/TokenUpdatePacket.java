package de.webtwob.mbma.common.packet;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.inventory.TokenContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.IOException;

/**
 * Created by bennet on 21.03.17.
 */
public class TokenUpdatePacket implements IMessage {

    private ItemStack request;

    public TokenUpdatePacket() {
    }

    public TokenUpdatePacket(ItemStack request) {
        this.request = request;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        PacketBuffer packetBuffer = new PacketBuffer(buf);
        try {
            request = packetBuffer.readItemStack();
        } catch (IOException e) {
            MBMALog.warn("Could not read ItemStack form TokenUpdatePacket");
        }

    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeItemStack(request);
    }

    public static class TokenUpdatePacketHandler implements IMessageHandler<TokenUpdatePacket, IMessage> {
        @Override
        public IMessage onMessage(TokenUpdatePacket message, MessageContext ctx) {
            EntityPlayer playerEntity = ctx.getServerHandler().playerEntity;
            if (playerEntity.openContainer instanceof TokenContainer) {
                ItemStack held = ((TokenContainer) playerEntity.openContainer).stack;
                ICraftingRequest icr;
                if ((icr = held.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
                    if (message.request != null) {
                        MBMALog.debug("Got TokenUpdatePacket for {}!"+message.request.getDisplayName());
                        icr.setRequest(message.request);
                    } else {
                        MBMALog.debug("Got TokenUpdatePacket to clear request!");
                        icr.setRequest(ItemStack.EMPTY);
                    }
                }
            }
            return null;
        }
    }
}
