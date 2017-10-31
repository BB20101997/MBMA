package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.multiblock.MultiBlockGroupManager;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityStorageIndexer extends MultiBlockTileEntity {
    
    @ObjectHolder("mbmacore:storage")
    public static final MultiBlockGroupManager MANAGER_STORAGE = null;


    @Override
    public MultiBlockGroupManager getManager() {
        return MANAGER_STORAGE;
    }
    
    @Override
    public void update() {
        super.update();
        
    }
}
