package de.webtwob.mma.core.common.proxy;

import de.webtwob.mma.core.MMACore;
import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.inventory.TokenGeneratorContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
public class CommonProxy implements IGuiHandler {
    
    public static final int TOKEN_GENERATOR_GUI     = 3;
    
    /**
     * registers this as this Mods GUIHandler
     */
    public void register() {
        CoreLog.debug("Registering GUIHandler");
        NetworkRegistry.INSTANCE.registerGuiHandler(MMACore.INSTANCE, this);
    }
    
    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (id) {
            case TOKEN_GENERATOR_GUI:
                return TokenGeneratorContainer.tryCreateInstance(player, tileEntity);
            default:
                CoreLog.LOGGER.error("MMACore Proxy for GUIHandling still used, use MMAAPI instead!");
                return null;
        }
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
