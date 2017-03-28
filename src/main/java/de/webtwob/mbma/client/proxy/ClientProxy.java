package de.webtwob.mbma.client.proxy;

import de.webtwob.mbma.client.gui.QSGui;
import de.webtwob.mbma.client.gui.TokenGeneratorGui;
import de.webtwob.mbma.client.gui.TokenGui;
import de.webtwob.mbma.common.item.MBMAItemList;
import de.webtwob.mbma.common.proxy.CommonProxy;
import de.webtwob.mbma.common.tileentity.QSTileEntity;
import de.webtwob.mbma.common.tileentity.TokenGeneratorTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
public class ClientProxy extends CommonProxy {

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case QS_GUI: {
                if (tileEntity != null && tileEntity instanceof QSTileEntity) {
                    return new QSGui(player.inventory, tileEntity.getCapability(CapabilityItemHandler
                            .ITEM_HANDLER_CAPABILITY, null));
                }
                break;
            }
            case TOKEN_GUI_MAIN_HAND:
            case TOKEN_GUI_OFF_HAND: {
                return new TokenGui(player.getHeldItem(EnumHand.values()[ID - 1]), player);
            }
            case TOKEN_GENERATOR_GUI: {
                if (tileEntity instanceof TokenGeneratorTileEntity) {
                    return new TokenGeneratorGui(player, (TokenGeneratorTileEntity) tileEntity);
                }
            }
        }
        return null;
    }

    @Override
    public void initModel() {
        MBMAItemList.initModels();
    }
}
