package de.webtwob.mma.api.capability;

import de.webtwob.mma.api.APILog;
import de.webtwob.mma.api.capability.implementations.DefaultBlockPosProvider;
import de.webtwob.mma.api.capability.implementations.DefaultCraftingRecipe;
import de.webtwob.mma.api.capability.implementations.DefaultCraftingRequest;
import de.webtwob.mma.api.capability.storage.*;
import de.webtwob.mma.api.interfaces.capability.*;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.ArrayList;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class APICapabilities {

    @CapabilityInject(IBlockPosProvider.class)
    public static final Capability<IBlockPosProvider> CAPABILITY_BLOCK_POS = null;
    @CapabilityInject(ICraftingRequest.class)
    public static final Capability<ICraftingRequest>  CAPABILITY_CRAFTING_REQUEST = null;
    @CapabilityInject(ICraftingRecipe.class)
    public static final Capability<ICraftingRecipe>   CAPABILITY_CRAFTING_RECIPE = null;
    @CapabilityInject(IPatternProvider.class)
    public static final Capability<IPatternProvider>  CAPABILITY_PATTERN_PROVIDER = null;

    private static boolean registered = false;

    private APICapabilities() {
    }

    /**
     * Call this in pre-Init
     */
    public static void register() {
        if (!registered) {
            registered = true;
            APILog.debug("Registering Capabilities");
            CapabilityManager.INSTANCE.register(
                    IBlockPosProvider.class, new BlockPosStorage(), DefaultBlockPosProvider::new);
            CapabilityManager.INSTANCE.register(
                    ICraftingRequest.class, new CraftingRequestStorage(), DefaultCraftingRequest::new);
            CapabilityManager.INSTANCE.register(
                    ICraftingRecipe.class, new CraftingRecipeStorage(), DefaultCraftingRecipe::new);
            CapabilityManager.INSTANCE.register(
                    ICraftingRequestProvider.class, new ICraftingRequestProviderIStorage(), () -> require -> null);
            CapabilityManager.INSTANCE.register(IPatternProvider.class, new IPatternProviderStorage(),
                                                () -> ArrayList::new
            );
        }
    }

}
