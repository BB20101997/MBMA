package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.multiblock.MultiBlockGroupManager;

import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityCraftingProcessor extends MultiBlockTileEntity {

    @GameRegistry.ObjectHolder("mbmacore:crafting")
    public static final MultiBlockGroupManager MANAGER_CRAFTING = null;



    @Override
    public MultiBlockGroupManager getManager() {
        return MANAGER_CRAFTING;
    }

}
