package de.webtwob.mbma.core.common.proxy;

import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.core.MBMACore;
import de.webtwob.mbma.core.common.CoreLog;
import de.webtwob.mbma.core.common.inventory.TokenContainer;
import de.webtwob.mbma.core.common.inventory.TokenGeneratorContainer;
import de.webtwob.mbma.core.common.tileentity.TileEntityRequestGenerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
    public static final int OFF_HAND_ITEM_GUI = 2;
    
    public static final int TOKEN_GENERATOR_GUI = 3;
    public static final int RECIPE_BANK_GUI = 4;
    
    protected static Capability<ICraftingRequest> requestCapability;
    
    @CapabilityInject(ICraftingRequest.class)
    private static void injectRequest(Capability<ICraftingRequest> requestCapability) {
        CommonProxy.requestCapability = requestCapability;
    }
    
    public void register() {
        CoreLog.debug("Registering GUIHandler");
        NetworkRegistry.INSTANCE.registerGuiHandler(MBMACore.INSTANCE, this);
    }
    
    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (id) {
            case MAIN_HAND_ITEM_GUI:
            case OFF_HAND_ITEM_GUI: {
                ItemStack held = player.getHeldItem(EnumHand.values()[id - 1]);
                if (requestCapability != null && held.hasCapability(requestCapability, null)) {
                    return new TokenContainer(player, EnumHand.values()[id - 1]);
                }
                break;
            }
            case TOKEN_GENERATOR_GUI: {
                if (tileEntity instanceof TileEntityRequestGenerator) {
                    return new TokenGeneratorContainer(player, (TileEntityRequestGenerator) tileEntity);
                }
                break;
            }
            default:
                return null;
        }
        return null;
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
