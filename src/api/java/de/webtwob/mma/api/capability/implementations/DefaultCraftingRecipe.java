package de.webtwob.mma.api.capability.implementations;

import de.webtwob.mma.api.enums.NBTMatchType;
import de.webtwob.mma.api.interfaces.capability.ICraftingRecipe;
import de.webtwob.mma.api.references.ResourceLocations;
import de.webtwob.mma.api.registries.RecipeType;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by BB20101997 on 16. Nov. 2017.
 */
public class DefaultCraftingRecipe implements ICraftingRecipe {

    ItemStack[]    inputs      = new ItemStack[0];
    ItemStack[]    outputs     = new ItemStack[0];
    NBTMatchType[] inputMatch  = new NBTMatchType[0];
    NBTMatchType[] outputMatch = new NBTMatchType[0];
    boolean[]      oreDict     = new boolean[0];
    double[]       chances     = new double[0];
    RecipeType type;

    @Override
    public void resizeInputCount(int i) {
        inputs = Arrays.copyOf(inputs, i);
        inputMatch = Arrays.copyOf(inputMatch, i);
        oreDict = Arrays.copyOf(oreDict, i);
    }

    @Override
    public void resizeOutputCount(int i) {
        outputs = Arrays.copyOf(outputs, i);
        outputMatch = Arrays.copyOf(outputMatch, i);
        chances = Arrays.copyOf(chances, i);
    }

    @Override
    public void setInput(int slot, @Nonnull ItemStack stack, boolean oreDict, @Nonnull NBTMatchType ignoreNBT) {
        if (slot >= inputs.length) {
            resizeInputCount(slot + 1);
        }
        inputs[slot] = stack;
        this.oreDict[slot] = oreDict;
        inputMatch[slot] = ignoreNBT;
    }

    @Override
    public void setOutput(int slot, @Nonnull ItemStack stack, double chance, @Nonnull NBTMatchType ignoreNBT) {
        if (slot >= outputs.length) {
            resizeOutputCount(slot + 1);
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

    @Nullable
    @Override
    public RecipeType getRecipeType() {
        return type != null ? type : GameRegistry.findRegistry(RecipeType.class)
                                                 .getValue(ResourceLocations.REG_RECIPE_CUSTOM);
    }

    @Override
    public void setRecipeType(@Nullable RecipeType r) {
        type = r;
    }
}
