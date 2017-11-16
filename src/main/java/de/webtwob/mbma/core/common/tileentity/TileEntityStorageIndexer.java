package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.interfaces.tileentity.IItemMoveRequest;
import de.webtwob.mbma.api.registries.MultiBlockGroupType;

import java.util.LinkedList;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityStorageIndexer extends MultiBlockTileEntity {
    
    @ObjectHolder("mbmacore:storage")
    public static final MultiBlockGroupType MANAGER_STORAGE = null;
    
    private LinkedList<IItemMoveRequest> requests = new LinkedList<>();


    @Override
    public MultiBlockGroupType getGroupType() {
        return MANAGER_STORAGE;
    }
    
    @Override
    public void update() {
        super.update();
        //TODO handle requests
        requests.forEach(IItemMoveRequest::passOnRequest);
    }
    
    /**
     * Adds a IItemMoveRequest to the queue of requests to be handled
     * Requests are handled on this TileEntity update Method being called  usually every Tick
     * @param request the request to add
     */
    public void addItemMoveRequest(IItemMoveRequest request){
        requests.add(request);
    }
    
}
