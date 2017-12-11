package de.webtwob.mma.api.inventory;

import de.webtwob.mma.api.APILog;
import de.webtwob.mma.api.interfaces.gui.IGUIHandlerServer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 11. Dez. 2017.
 */
public class ApiCommonProxy implements IGuiHandler {
    
    
    public static final int MAIN_HAND_ITEM_GUI = 0;
    public static final int OFF_HAND_ITEM_GUI = 1;
    public static final int TILE_ENTITY_GUI = 2;
    public static final int BLOCK_GUI = 3;
    
    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        Object guiProvider = getCorrespondingObject(id,player,world,x,y,z);
        if (guiProvider instanceof IGUIHandlerServer) {
            return ((IGUIHandlerServer) guiProvider).getServerGuiElement(id, player, world, x, y, z);
        }
        return null;
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
    
    protected Object getCorrespondingObject(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case MAIN_HAND_ITEM_GUI:
                return player.getHeldItem(EnumHand.MAIN_HAND).getItem();
            case OFF_HAND_ITEM_GUI:
                return player.getHeldItem(EnumHand.OFF_HAND).getItem();
            case TILE_ENTITY_GUI:
                return world.getTileEntity(new BlockPos(x, y, z));
            case BLOCK_GUI:
                return world.getBlockState(new BlockPos(x, y, z)).getBlock();
            default:
                APILog.LOGGER.error("Unsupported id {} in ApiCommonProxy!", id);
                return null;
        }
    }
}
