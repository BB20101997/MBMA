package de.webtwob.mma.api.inventory;

import de.webtwob.mma.api.interfaces.gui.IGUIHandlerClient;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 11. Dez. 2017.
 */
@SuppressWarnings("unused")
public class ApiClientProxy extends ApiCommonProxy {

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        Object guiProvider = getCorrespondingObject(id, player, world, x, y, z);
        if (guiProvider instanceof IGUIHandlerClient) {
            return ((IGUIHandlerClient) guiProvider).getClientGuiElement(id, player, world, x, y, z);
        }
        return null;
    }
}
