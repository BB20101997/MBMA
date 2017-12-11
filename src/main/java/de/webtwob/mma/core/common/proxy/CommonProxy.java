package de.webtwob.mma.core.common.proxy;

import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.core.MMACore;
import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.inventory.CraftingControllerContainer;
import de.webtwob.mma.core.common.inventory.TokenContainer;
import de.webtwob.mma.core.common.inventory.TokenGeneratorContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
public class CommonProxy implements IGuiHandler {
    
    public static final int QS_GUI = 0;
    
    public static final int MAIN_HAND_ITEM_GUI = 1;
    public static final int OFF_HAND_ITEM_GUI  = 2;
    
    public static final int TOKEN_GENERATOR_GUI     = 3;
    public static final int CRAFTING_CONTROLLER_GUI = 4;
    
    protected static Capability<ICraftingRequest> requestCapability;
    
    @CapabilityInject(ICraftingRequest.class)
    private static void injectRequest(Capability<ICraftingRequest> requestCapability) {
        CommonProxy.requestCapability = requestCapability;
    }
    
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
            case MAIN_HAND_ITEM_GUI:
            case OFF_HAND_ITEM_GUI:
                //TODO maybe instead add an interface for Items with InHandGui with functions to create GUI'S
                return TokenContainer.tryCreateInstance(player, EnumHand.values()[id - 1]);
            case TOKEN_GENERATOR_GUI:
                return TokenGeneratorContainer.tryCreateInstance(player, tileEntity);
            case CRAFTING_CONTROLLER_GUI:
                return CraftingControllerContainer.tryCreateInstance(player, tileEntity);
            default:
                return null;
        }
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
