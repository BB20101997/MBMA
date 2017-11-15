package de.webtwob.mbma.core.client.proxy;

import de.webtwob.mbma.core.client.gui.TokenGeneratorGui;
import de.webtwob.mbma.core.client.gui.TokenGui;
import de.webtwob.mbma.core.common.proxy.CommonProxy;
import de.webtwob.mbma.core.common.tileentity.TileEntityRequestGenerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
public class ClientProxy extends CommonProxy {
    
    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (id) {
            case MAIN_HAND_ITEM_GUI:
            case OFF_HAND_ITEM_GUI: {
                ItemStack held = player.getHeldItem(EnumHand.values()[id - 1]);
                if (requestCapability != null && held.hasCapability(requestCapability, null)) {
                        return new TokenGui(player,EnumHand.values()[id-1]);
                }
                //add other Items here possibly
                break;
            }
            case TOKEN_GENERATOR_GUI: {
                if (tileEntity instanceof TileEntityRequestGenerator) {
                    return new TokenGeneratorGui(player, (TileEntityRequestGenerator) tileEntity);
                }
                break;
            }
            default:
                //no matching gui
                return null;
        }
        return null;
    }
}
