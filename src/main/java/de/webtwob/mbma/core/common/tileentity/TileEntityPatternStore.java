package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.registries.MultiBlockGroupType;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityPatternStore extends MultiBlockTileEntity {
    
    @ObjectHolder("mbmacore:recipes")
    public static final MultiBlockGroupType MANAGER_RECIPES = null;


    @Override
    public MultiBlockGroupType getGroupType() {
        return MANAGER_RECIPES;
    }
}
