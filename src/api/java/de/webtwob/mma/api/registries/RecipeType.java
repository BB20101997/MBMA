package de.webtwob.mma.api.registries;

import de.webtwob.mma.api.APILog;
import de.webtwob.mma.api.enums.NBTMatchType;
import de.webtwob.mma.api.interfaces.capability.ICraftingRecipe;

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

    /**
     * @param autoFill whether  this can RecipeType automatically determine the Output from the Input
     */
    public RecipeType(boolean autoFill) {
        autoFillAble = autoFill;
    }

    /**
     * Will be called after the Type of a ICraftingRecipe was changed
     *
     * @param icr the ICraftingRecipe who's type was changed
     */
    abstract void configureICraftingRecipe(ICraftingRecipe icr);

    /**
     * @return if this RecipeType can automatically determine the Output from the Input
     */
    public final boolean canAutoFillOutput() {
        return autoFillAble;
    }

    /**
     * @param request the recipe for which to determine the output
     * @param world the world the player is in
     */
    public void autoFill(ICraftingRecipe request, World world) {
        if (!canAutoFillOutput()) {
            throw new UnsupportedOperationException("autoFill was called on a RecipeType that was not autofillable");
        }else{
            APILog.error("Autofillable RecipeType did not override autoFill or did call super!");
        }
    }

    public static class VanillaCraftingRecipeType extends RecipeType {

        VanillaCraftingRecipeType() {
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

    public static class CustomRecipeType extends RecipeType {

        CustomRecipeType() {
            super(false);
        }

        @Override
        public void configureICraftingRecipe(ICraftingRecipe icr) {
            //don't expand or truncate the recipe we don't know how big it should be and we don't want to mess stuff up
        }
    }

}
