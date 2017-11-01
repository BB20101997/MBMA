package de.webtwob.mbma.core.client.proxy;

import de.webtwob.mbma.core.client.gui.RecipeBankGui;
import de.webtwob.mbma.core.client.gui.TokenGeneratorGui;
import de.webtwob.mbma.core.client.gui.TokenGui;
import de.webtwob.mbma.core.common.proxy.CommonProxy;
import de.webtwob.mbma.core.common.tileentity.TileEntityRequestGenerator;
import de.webtwob.mbma.core.common.tileentity.old.TileEntityRecipeStoreOld;

import net.minecraft.entity.player.EntityPlayer;
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
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case MAIN_HAND_ITEM_GUI:
            case OFF_HAND_ITEM_GUI: {
                return new TokenGui(player.getHeldItem(EnumHand.values()[ID - 1]), player);
            }
            case TOKEN_GENERATOR_GUI: {
                if (tileEntity instanceof TileEntityRequestGenerator) {
                    return new TokenGeneratorGui(player, (TileEntityRequestGenerator) tileEntity);
                }
            }
            case RECIPE_BANK_GUI: {
                if (tileEntity instanceof TileEntityRecipeStoreOld) {
                    return new RecipeBankGui(player, (TileEntityRecipeStoreOld) tileEntity);
                }
            }
        }
        return null;
    }
}
