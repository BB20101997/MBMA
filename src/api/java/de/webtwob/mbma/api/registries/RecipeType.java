package de.webtwob.mbma.api.registries;

import de.webtwob.mbma.api.APILog;
import de.webtwob.mbma.api.enums.NBTMatchType;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRecipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 11. Nov. 2017.
 */
public abstract class RecipeType extends IForgeRegistryEntry.Impl<RecipeType> {
    
    private boolean autoFillAble;
    
    public RecipeType(boolean autoFill) {
        autoFillAble = autoFill;
    }
    
    /**
     * Will be called after the Type of a ICraftingRecipe was changed
     *
     * @param icr the ICraftingRecipe who's type was changed
     */
    abstract void configureICraftingRecipe(ICraftingRecipe icr);
    
    public final boolean canAutoFillOutput() {
        return autoFillAble;
    }
    
    public void autoFill(ICraftingRecipe request, World world) {
        if (!canAutoFillOutput()) {
            throw new UnsupportedOperationException("autoFill was called on a RecipeType that was not autofillable");
        }else{
            APILog.warn("Autofillable RecipeType did not override autoFill or did call super!");
        }
    }
    
    public static class VanillaCraftingRecipeType extends RecipeType {
        
        public VanillaCraftingRecipeType() {
            super(true);
        }
    
        @Override
        public void autoFill(ICraftingRecipe request, World world) {
            if (canAutoFillOutput()) {
                request.setOutput(0, CraftingManager.findMatchingResult(getVanillaMatrixForRequest(request), world), 1, NBTMatchType.FULL);
            }
        }
        
        @Nonnull
        private InventoryCrafting getVanillaMatrixForRequest(@Nonnull ICraftingRecipe request){
            InventoryCrafting matrix =  new InventoryCrafting(new Container() {
                @Override
                public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
                    return true;
                }
            },3,3);
    
            ItemStack[] ins = request.getInputs();
            for(int i = 0 ;i<9&&i<ins.length;i++) {
                matrix.setInventorySlotContents(i,ins[i]);
            }
            return matrix;
        }
        
        @Override
        void configureICraftingRecipe(ICraftingRecipe icr) {
            icr.resizeOutputCount(1);
            icr.resizeInputCount(9);
        }
        
    }
    
    public static class CustomtRecipeType extends RecipeType {
        
        CustomtRecipeType() {
            super(false);
        }
        
        @Override
        public void configureICraftingRecipe(ICraftingRecipe icr) {
            //don't expand or truncate the recipe we don't know how big it should be and we don't want to mess stuff up
        }
    }
    
}
