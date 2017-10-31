package de.webtwob.mbma.api.capability;

import de.webtwob.mbma.api.MBMAAPILog;
import de.webtwob.mbma.api.capability.factory.BlockPosFactory;
import de.webtwob.mbma.api.capability.factory.CraftingRecipeFactory;
import de.webtwob.mbma.api.capability.factory.CraftingRequestFactory;
import de.webtwob.mbma.api.capability.storage.BlockPosStorage;
import de.webtwob.mbma.api.capability.storage.CraftingRecipeStorage;
import de.webtwob.mbma.api.capability.storage.CraftingRequestStorage;
import de.webtwob.mbma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRecipe;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class APICapabilities {
    
    @CapabilityInject(IBlockPosProvider.class)
    public static Capability<IBlockPosProvider> CAPABILITY_BLOCK_POS = null;
    @CapabilityInject(ICraftingRequest.class)
    public static Capability<ICraftingRequest> CAPABILITY_CRAFTING_REQUEST = null;
    @CapabilityInject(ICraftingRecipe.class)
    public static Capability<ICraftingRecipe> CAPABILITY_CRAFTING_RECIPE = null;
    private static boolean registered = false;
    
    private APICapabilities() {
    }
    
    /**
     * Call this in pre-Init
     */
    public static void register() {
        if (!registered) {
            registered = true;
            MBMAAPILog.debug("Registering Capabilities");
            CapabilityManager.INSTANCE.register(IBlockPosProvider.class, new BlockPosStorage(), new BlockPosFactory());
            CapabilityManager.INSTANCE.register(
                    ICraftingRequest.class, new CraftingRequestStorage(), new CraftingRequestFactory());
            CapabilityManager.INSTANCE.register(
                    ICraftingRecipe.class, new CraftingRecipeStorage(), new CraftingRecipeFactory());
        }
    }
}
