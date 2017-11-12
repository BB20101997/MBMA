package de.webtwob.mbma.api.capability.factory;

import de.webtwob.mbma.api.enums.NBTMatchType;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRecipe;
import de.webtwob.mbma.api.registries.RecipeType;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Created by bennet on 03.04.17.
 */
public class CraftingRecipeFactory implements Callable<ICraftingRecipe> {
    
    @Override
    public ICraftingRecipe call() {
        return new ICraftingRecipe() {
            
            ItemStack[] inputs = new ItemStack[0];
            ItemStack[] outputs = new ItemStack[0];
            NBTMatchType[] inputMatch = new NBTMatchType[0];
            NBTMatchType[] outputMatch = new NBTMatchType[0];
            boolean[] oreDict = new boolean[0];
            double[] chances = new double[0];
            RecipeType type;
            
            
            @Override
            public void resizeInputCount(int i) {
                inputs = Arrays.copyOf(inputs,i);
                inputMatch = Arrays.copyOf(inputMatch,i);
                oreDict = Arrays.copyOf(oreDict,i);
            }
    
            @Override
            public void resizeOutputCount(int i) {
                outputs = Arrays.copyOf(outputs,i);
                outputMatch = Arrays.copyOf(outputMatch, i);
                chances = Arrays.copyOf(chances,i);
            }
    
            @Override
            public void setInput(int slot, @Nonnull ItemStack stack, boolean oredict, @Nonnull NBTMatchType ignoreNBT) {
                if(slot>=inputs.length){
                    resizeInputCount(slot+1);
                }
                inputs[slot] = stack;
                oreDict[slot] = oredict;
                inputMatch[slot] = ignoreNBT;
            }
    
            @Override
            public void setOutput(int slot, @Nonnull ItemStack stack, double chance, @Nonnull NBTMatchType ignoreNBT) {
                if(slot>=outputs.length){
                    resizeOutputCount(slot+1);
                }
                outputs[slot] = stack;
                chances[slot] = chance;
                outputMatch[slot] = ignoreNBT;
            }
    
            @Nonnull
            @Override
            public ItemStack[] getInputs() {
                return inputs;
            }
    
            @Nonnull
            @Override
            public ItemStack[] getOutputs() {
                return outputs;
            }
    
            @Override
            public double[] getChance() {
                return chances;
            }
    
            @Nonnull
            @Override
            public boolean[] isInputOrdict() {
                return oreDict;
            }
    
            @Nonnull
            @Override
            public NBTMatchType[] shouldNBTBeIgnoredForInput() {
                return inputMatch;
            }
    
            @Nonnull
            @Override
            public NBTMatchType[] shouldNBTBeIgnoredForOutput() {
                return outputMatch;
            }
    
            @Nonnull
            @Override
            public RecipeType getRecipeType() {
                //TODO default to CustomRecipeType
                return type!=null?type:new RecipeType(false);
            }
    
            @Nonnull
            @Override
            public void setRecipeType(RecipeType r) {
                type = r;
            }
        };
    }
}
