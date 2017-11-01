package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.registries.MultiBlockGroupType;

import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityQueue extends MultiBlockTileEntity{
    @GameRegistry.ObjectHolder("mbmacore:queue")
    public static final MultiBlockGroupType MANAGER_QUEUE= null;
    
    
    public MultiBlockGroupType getGroupType() {
        return MANAGER_QUEUE;
    }
}
