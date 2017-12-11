package de.webtwob.mma.core.client.proxy;

import de.webtwob.mma.core.client.gui.CraftingControllerGui;
import de.webtwob.mma.core.client.gui.TokenGeneratorGui;
import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.proxy.CommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {
    
    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (id) {
            case MAIN_HAND_ITEM_GUI:
            case OFF_HAND_ITEM_GUI:
                CoreLog.LOGGER.error("MAIN/OFF_HANDITEMGUI from MMACore still used, use MMAAPI instead!");
                return null;
            case TOKEN_GENERATOR_GUI:
                //TODO move as with Main and Off Hand
                return TokenGeneratorGui.tryCreateInstance(player,tileEntity);
            case CRAFTING_CONTROLLER_GUI:
                //TODO move as with Main and Off Hand
                return CraftingControllerGui.tryCreateInstance(player,tileEntity);
            default:
                return null;
        }
    }
}
