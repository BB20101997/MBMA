package de.webtwob.mbma.common.inventory;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.common.interfaces.IStackFilter;

/**
 * Created by BB20101997 on 29. Mär. 2017.
 */
public class MBMAFilter {

    private MBMAFilter(){}


    public static final IStackFilter OUTPUT_FILTER = (e) -> false;
    public static final IStackFilter MUSTER_FILTER = (e) -> {
        ICraftingRequest request;
        if (e != null && (request = e.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
            if (!request.getRequest().isEmpty()) {
                return true;
            }
        }
        return false;
    };
    public static final IStackFilter INPUT_FILTER  = (e) -> {
        ICraftingRequest request;
        if (e != null && (request = e.getCapability(APICapabilities.CAPABILITY_CRAFTING_REQUEST, null)) != null) {
            if (request.getRequest().isEmpty() || request.isCompleted()) {
                return true;
            }
        }
        return false;
    };

    public static final IStackFilter LINK_FILTER   = stack -> stack.hasCapability(APICapabilities.CAPABILITY_BLOCK_POS, null);
}
