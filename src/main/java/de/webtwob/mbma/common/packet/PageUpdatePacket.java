package de.webtwob.mbma.common.packet;

import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.inventory.RecipeBankContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by bennet on 01.04.17.
 */
public class PageUpdatePacket implements IMessage {
    
    private int type = 0;
    private String name;
    private int    id;
    
    public PageUpdatePacket() {}
    
    public static PageUpdatePacket createDeletePagePacket() {
        PageUpdatePacket packet = new PageUpdatePacket();
        packet.type = 0;
        return packet;
    }
    
    public static PageUpdatePacket createRenamePagePacket(String newName) {
        PageUpdatePacket packet = new PageUpdatePacket();
        packet.type = 1;
        packet.name = newName;
        return packet;
    }
    
    public static PageUpdatePacket createCreatePagePacket(String name) {
        PageUpdatePacket packet = new PageUpdatePacket();
        packet.type = 2;
        packet.name = name;
        return packet;
    }
    
    public static PageUpdatePacket createSwichPagePacket(int id) {
        PageUpdatePacket packet = new PageUpdatePacket();
        packet.type = 3;
        packet.id = id;
        return packet;
    }
    
    @Override
    public void fromBytes(final ByteBuf buf) {
        type = buf.readInt();
        switch(type){
            case 1:
            case 2:{
                byte[] nameBytes = new byte[buf.readInt()];
                buf.readBytes(nameBytes);
                name = new String(nameBytes);
                break;
            }
            case 3:{
                id = buf.readInt();
            }
        }
    }
    
    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(type);
        switch(type){
            case 1:
            case 2:{
                byte[] nameBytes = name.getBytes();
                buf.writeInt(nameBytes.length);
                buf.writeBytes(nameBytes);
                break;
            }
            case 3:{
                buf.writeInt(id);
            }
        }
    }
    
    public static class PageUpdatePacketHandler implements IMessageHandler<PageUpdatePacket, IMessage> {
        
        @Override
        public IMessage onMessage(final PageUpdatePacket message, final MessageContext ctx) {
            Container cont;
            if(ctx.side == Side.SERVER) {
                cont = ctx.getServerHandler().playerEntity.openContainer;
            } else {
                cont = Minecraft.getMinecraft().player.openContainer;
            }
            if(cont instanceof RecipeBankContainer) {
                switch(message.type){
                    case 0:{
                        ((RecipeBankContainer) cont).removePage();
                        MBMALog.info("Received DeletePagePacket!");
                        break;
                    }
                    case 1:{
                        ((RecipeBankContainer) cont).renamePage(message.name);
                        MBMALog.info("Received RenamePagePacket");
                        break;
                    }
                    case 2:{
                        ((RecipeBankContainer) cont).createPage(message.name);
                        MBMALog.info("Received CreatePagePacket");
                        break;
                    }
                    case 3:{
                        ((RecipeBankContainer) cont).setPage(message.id);
                        MBMALog.info("Received SwitchPagePacket");
                    }
                }
            }
            if(ctx.side == Side.SERVER) {
                return message;
            }
            
            return null;
        }
    }
}
