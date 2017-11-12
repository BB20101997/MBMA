package de.webtwob.mbma.api.registries;

import de.webtwob.mbma.api.enums.NBTMatchType;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRecipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Created by BB20101997 on 11. Nov. 2017.
 */
public class RecipeType extends IForgeRegistryEntry.Impl<RecipeType> {

    private boolean autoFillAble;
    
    public RecipeType(boolean autoFill){
        autoFillAble = autoFill;
    }
    
    /**
     * Will be called after the Type of a ICraftingRecipe was changed
     *
     * @param icr the ICraftingRecipe who's type was changed
     * */
    public void configureICraftingRecipe(ICraftingRecipe icr){
        icr.resizeOutputCount(1);
        icr.resizeInputCount(9);
    }
    
    boolean canAutFillOutput(){
        return autoFillAble;
    }
    
    void autoFill(ICraftingRecipe request, InventoryCrafting matrix, World world){
        if(canAutFillOutput()){
            request.setOutput(0, CraftingManager.findMatchingResult(matrix,world),1,NBTMatchType.FULL);
        }
    }
    
    
}
