package de.webtwob.mma.core.common.references;

import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mma.api.interfaces.capability.ICraftingRequestProvider;
import de.webtwob.mma.api.interfaces.capability.IPatternProvider;
import de.webtwob.mma.api.interfaces.tileentity.IMoveRequestProcessor;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

public class CapabilityInjections {

    private static Capability<IItemHandler>             capabilityItemHandler;
    private static Capability<ICraftingRequestProvider> capabilityRequestProvider;
    private static Capability<IPatternProvider>         capabilityPatternProvider;
    private static Capability<ICraftingRequest>         capabilityRequest;
    private static Capability<IMoveRequestProcessor>    capabilityRequestProcessor;

    private CapabilityInjections() {}

    public static Capability<ICraftingRequest> getCapabilityRequest() {
        return capabilityRequest;
    }

    public static Capability<IItemHandler> getCapabilityItemHandler() {
        return capabilityItemHandler;
    }

    public static Capability<ICraftingRequestProvider> getCapabilityRequestProvider() {
        return capabilityRequestProvider;
    }

    public static Capability<IPatternProvider> getCapabilityPatternProvider() {
        return capabilityPatternProvider;
    }

    public static Capability<IMoveRequestProcessor> getCapabilityRequestProcessor() {
        return capabilityRequestProcessor;
    }

    @CapabilityInject(IPatternProvider.class)
    private static void setCapabilityPatternProvider(Capability<IPatternProvider> handler) {
        CapabilityInjections.capabilityPatternProvider = handler;
    }

    @CapabilityInject(ICraftingRequest.class)
    private static void injectRequest(Capability<ICraftingRequest> handler) {
        capabilityRequest = handler;
    }

    @CapabilityInject(IItemHandler.class)
    private static void injectItemHandler(Capability<IItemHandler> handler) {
        capabilityItemHandler = handler;
    }

    @CapabilityInject(ICraftingRequestProvider.class)
    private static void injectCraftingRequestProvider(Capability<ICraftingRequestProvider> handler) {
        capabilityRequestProvider = handler;
    }

    @CapabilityInject(IMoveRequestProcessor.class)
    public static void injectRequestProcessor(Capability<IMoveRequestProcessor> handler) {
        capabilityRequestProcessor = handler;
    }
}
