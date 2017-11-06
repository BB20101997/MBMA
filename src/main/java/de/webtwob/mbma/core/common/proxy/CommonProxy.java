package de.webtwob.mbma.core.common.proxy;

import de.webtwob.mbma.core.MBMA_CORE;
import de.webtwob.mbma.core.common.MBMALog;
import de.webtwob.mbma.core.common.inventory.RecipeBankContainer;
import de.webtwob.mbma.core.common.inventory.TokenContainer;
import de.webtwob.mbma.core.common.inventory.TokenGeneratorContainer;
import de.webtwob.mbma.core.common.registration.Items;
import de.webtwob.mbma.core.common.tileentity.TileEntityRequestGenerator;
import de.webtwob.mbma.core.common.tileentity.old.TileEntityRecipeStoreOld;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    public void register() {
        MBMALog.debug("Registering GUIHandler");
        NetworkRegistry.INSTANCE.registerGuiHandler(MBMA_CORE.INSTANCE, this);
    }
    
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID) {
            case MAIN_HAND_ITEM_GUI:
            case OFF_HAND_ITEM_GUI: {
                ItemStack held = player.getHeldItem(EnumHand.values()[ID - 1]);
                if (held.getItem() == Items.TOKEN) {
                    return new TokenContainer(player.getHeldItem(EnumHand.values()[ID - 1]));
                }
            }
            case TOKEN_GENERATOR_GUI: {
                if (tileEntity instanceof TileEntityRequestGenerator) {
                    return new TokenGeneratorContainer(player, (TileEntityRequestGenerator) tileEntity);
                }
            }
            case RECIPE_BANK_GUI: {
                if (tileEntity instanceof TileEntityRecipeStoreOld) {
                    return new RecipeBankContainer(player, (TileEntityRecipeStoreOld) tileEntity);
                }
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
