package de.webtwob.mma.core.common.inventory;

import de.webtwob.mma.api.crafting.ItemStackContainer;
import de.webtwob.mma.api.interfaces.capability.ICraftingRequest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nonnull;

/**
 * Created by bennet on 21.03.17.
 */
public class TokenContainer extends Container {

    private static Capability<ICraftingRequest> requestCapability;
    @Nonnull
    public final ItemStack stack;
    private final EnumHand hand;
    private ItemStackContainer container = new ItemStackContainer();

    public TokenContainer(@Nonnull EntityPlayer player, EnumHand enumHand) {
        stack = player.getHeldItem(enumHand);
        hand = enumHand;
        if (requestCapability != null) {
            ICraftingRequest request = stack.getCapability(requestCapability, null);
            if (request != null) {
                container.setItemStack(request.getRequest());
            }
        }
    }

    public ResourceLocation getRequestRegistryName(){
        return container.getItemStack().getItem().getRegistryName();
    }

    @CapabilityInject(ICraftingRequest.class)
    private static void injectRequest(Capability<ICraftingRequest> requestCapability) {
        TokenContainer.requestCapability = requestCapability;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return stack.equals(playerIn.getHeldItem(hand)) && requestCapability != null && stack.getCapability(requestCapability, null) != null;
    }


    public int getRequestAmount() {
        ICraftingRequest request = ICraftingRequest.getCraftingRequest(stack);
        if(request!=null){
            return request.getQuantity();
        }
        return 0;
    }

    public void setItem(Item item) {
        ICraftingRequest request = ICraftingRequest.getCraftingRequest(stack);
        if(request!=null){
            request.setRequest(new ItemStack(item,request.getQuantity()));
        }
    }

    public void setAmount(int amount) {
        ICraftingRequest request = ICraftingRequest.getCraftingRequest(stack);
        if(request!=null){
            request.setQuantity(amount);
        }
    }
    
    public static TokenContainer tryCreateInstance(final EntityPlayer player, final EnumHand enumHand) {
        ItemStack held = player.getHeldItem(enumHand);
        if (requestCapability != null && held.hasCapability(requestCapability, null)) {
            return new TokenContainer(player, enumHand);
        }
        return null;
    }
}
