package de.webtwob.mbma.client.proxy;

import de.webtwob.mbma.client.gui.QSGui;
import de.webtwob.mbma.common.proxy.CommonProxy;
import de.webtwob.mbma.common.tileentity.QSTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
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
        if(tileEntity != null && tileEntity instanceof QSTileEntity) {
            return new QSGui(player.inventory, tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null));
        }
        return null;
    }
}
