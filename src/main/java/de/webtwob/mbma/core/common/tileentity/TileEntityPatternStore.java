package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.multiblock.MultiBlockGroupManager;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityPatternStore extends MultiBlockTileEntity {
    
    @ObjectHolder("mbmacore:recipes")
    public static final MultiBlockGroupManager MANAGER_RECIPES = null;


    @Override
    public MultiBlockGroupManager getManager() {
        return MANAGER_RECIPES;
    }
}
