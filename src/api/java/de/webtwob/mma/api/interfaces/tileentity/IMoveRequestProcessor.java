package de.webtwob.mma.api.interfaces.tileentity;

import de.webtwob.mma.api.capability.APICapabilities;

import net.minecraft.tileentity.TileEntity;

public interface IMoveRequestProcessor {

    static IMoveRequestProcessor getMoveRequestProcessor(TileEntity tileEntity) {
        if (APICapabilities.CAPABILITY_REQUEST_PROCESSOR == null || tileEntity == null) {
            return null;
        }
        return tileEntity.getCapability(APICapabilities.CAPABILITY_REQUEST_PROCESSOR, null);
    }

    /**
     * Adds a IItemMoveRequest to the queue of requests to be handled
     * Requests are handled on this TileEntity update Method being called  usually every Tick
     *
     * @param request the request to add
     */
    void addItemMoveRequest(IItemMoveRequest request);
}
