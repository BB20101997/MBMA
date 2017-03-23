package de.webtwob.mbma.common.proxy;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.inventory.QSContainer;
import de.webtwob.mbma.common.inventory.TokenContainer;
import de.webtwob.mbma.common.tileentity.QSTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
public class CommonProxy implements IGuiHandler {

    public void register() {
        MBMALog.debug("Registering GUIHandler");
        NetworkRegistry.INSTANCE.registerGuiHandler(MultiblockMaschineAutomation.INSTANCE, this);
    }

    public void initModel(){

    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0: {
                TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
                if (tileEntity != null && tileEntity instanceof QSTileEntity) {
                    MBMALog.debug("GUIOpened at {},{},{}", x, y, z);
                    return new QSContainer(player.inventory, tileEntity.getCapability(ITEM_HANDLER_CAPABILITY, null), player);
                }
                break;
            }
            case 1:
            case 2: {
                return new TokenContainer(player.getHeldItem(EnumHand.values()[ID - 1]));
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
