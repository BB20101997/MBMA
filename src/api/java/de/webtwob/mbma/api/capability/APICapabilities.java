package de.webtwob.mbma.api.capability;

import de.webtwob.mbma.api.APILog;
import de.webtwob.mbma.api.capability.factory.BlockPosFactory;
import de.webtwob.mbma.api.capability.factory.CraftingRecipeFactory;
import de.webtwob.mbma.api.capability.factory.CraftingRequestFactory;
import de.webtwob.mbma.api.capability.storage.BlockPosStorage;
import de.webtwob.mbma.api.capability.storage.CraftingRecipeStorage;
import de.webtwob.mbma.api.capability.storage.CraftingRequestStorage;
import de.webtwob.mbma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRecipe;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequestProvider;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public class APICapabilities {
    
    @CapabilityInject(IBlockPosProvider.class)
    public static final Capability<IBlockPosProvider> CAPABILITY_BLOCK_POS = null;
    @CapabilityInject(ICraftingRequest.class)
    public static final Capability<ICraftingRequest> CAPABILITY_CRAFTING_REQUEST = null;
    @CapabilityInject(ICraftingRecipe.class)
    public static final Capability<ICraftingRecipe> CAPABILITY_CRAFTING_RECIPE = null;
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
            CapabilityManager.INSTANCE.register(IBlockPosProvider.class, new BlockPosStorage(), new BlockPosFactory());
            CapabilityManager.INSTANCE.register(
                    ICraftingRequest.class, new CraftingRequestStorage(), new CraftingRequestFactory());
            CapabilityManager.INSTANCE.register(
                    ICraftingRecipe.class, new CraftingRecipeStorage(), new CraftingRecipeFactory());
            CapabilityManager.INSTANCE.register(ICraftingRequestProvider.class, new ICraftingRequestProviderIStorage(),()-> require -> null);
        }
    }
    
    private static class ICraftingRequestProviderIStorage implements Capability.IStorage<ICraftingRequestProvider> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ICraftingRequestProvider> capability, ICraftingRequestProvider instance, EnumFacing side) {
            //by default we don't store stuff in the Provider
            return null;
        }
        
        @Override
        public void readNBT(Capability<ICraftingRequestProvider> capability, ICraftingRequestProvider instance, EnumFacing side, NBTBase nbt) {
            //by default we won't don't store stuff so we can't read stuff
        }
    }
}
