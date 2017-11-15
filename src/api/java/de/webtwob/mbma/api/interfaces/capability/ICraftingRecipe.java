package de.webtwob.mbma.api.interfaces.capability;

import de.webtwob.mbma.api.references.NBTKeys;
import de.webtwob.mbma.api.enums.NBTMatchType;
import de.webtwob.mbma.api.registries.RecipeType;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * Created by bennet on 03.04.17.
 */
public interface ICraftingRecipe extends INBTSerializable<NBTTagCompound> {
    
    void resizeInputCount(int i);
    
    void resizeOutputCount(int i);
    
    void setInput(int slot, @Nonnull ItemStack stack, boolean oredict, @Nonnull NBTMatchType ignoreNBT);
    
    void setOutput(int slot, @Nonnull ItemStack stack, double chance, @Nonnull NBTMatchType ignoreNBT);
    
    @Nonnull
    ItemStack[] getInputs();
    
    @Nonnull
    ItemStack[] getOutputs();
    
    double[] getChance();
    
    @Nonnull
    boolean[] isInputOrdict();
    
    @Nonnull
    NBTMatchType[] shouldNBTBeIgnoredForInput();
    
    @Nonnull
    NBTMatchType[] shouldNBTBeIgnoredForOutput();
    
    @Nonnull
    RecipeType getRecipeType();
    
    @Nonnull
    void setRecipeType(RecipeType r);
    
    @Override
    default NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        
        NBTTagList inputItems = new NBTTagList();
        NBTTagList outputItems = new NBTTagList();
        NBTTagList oreDictInput = new NBTTagList();
        NBTTagList outputChange = new NBTTagList();
        int[] matchesOrdinalInput;
        int[] matchesOrdinalOutput;
        
        
        boolean[] oreDictArray = isInputOrdict();
        for (boolean oreDict : oreDictArray) {
            oreDictInput.appendTag(new NBTTagByte((byte) (oreDict ? 1 : 0)));
        }
        
        
        double[] chance = getChance();
        for (double d : chance) {
            outputChange.appendTag(new NBTTagDouble(d));
        }
        
        
        NBTMatchType[] matchInput = shouldNBTBeIgnoredForInput();
        matchesOrdinalInput = new int[matchInput.length];
        Arrays.setAll(matchesOrdinalInput, i -> matchInput[i].ordinal());
        
        
        NBTMatchType[] matchOutput = shouldNBTBeIgnoredForOutput();
        matchesOrdinalOutput = new int[matchOutput.length];
        Arrays.setAll(matchesOrdinalOutput, i -> matchOutput[i].ordinal());
        
        
        //noinspection ConstantConditions
        ResourceLocation type = getRecipeType().getRegistryName();
        if(type!=null) {
            compound.setString(NBTKeys.ICRAFT_RECIPE_TYPE, type.toString());
        }else{
            //TODO default to custom type
            //compound.setString(NBTKeys.ICRAFT_RECIPE_TYPE, "//TODO put default here");
        }
        compound.setTag(NBTKeys.ICRAFT_RECIPE_STACKS_IN, inputItems);
        compound.setTag(NBTKeys.ICRAFT_RECIPE_OREDICT_LIST, oreDictInput);
        compound.setIntArray(NBTKeys.ICRAFT_RECIPE_NBT_IN, matchesOrdinalInput);
        
        compound.setTag(NBTKeys.ICRAFT_RECIPE_STACKS_OUT, outputItems);
        compound.setTag(NBTKeys.ICRAFT_RECIPE_CHANCE_OUT, outputChange);
        compound.setIntArray(NBTKeys.ICRAFT_RECIPE_NBT_OUT, matchesOrdinalOutput);
        
        return compound;
    }
    
    @Override
    default void deserializeNBT(final NBTTagCompound nbt) {
        if (nbt != null) {
            if (nbt.hasKey(NBTKeys.ICRAFT_RECIPE_TYPE, Constants.NBT.TAG_STRING)) {
                setRecipeType(GameRegistry.findRegistry(RecipeType.class).getValue(new ResourceLocation(nbt.getString(NBTKeys.ICRAFT_RECIPE_TYPE))));
            } else {
                //TODO default to custom TYPE
            }
            
            deserializeInputs(nbt);
            deserializeOutputs(nbt);
        }
    }
    
     /**
     * Used to spilt the deserialization into smaller chunks
     * this one handles the deserialization of all the state that has to do with the Outputs of this Recipe
     *
     * @param nbt the NBTTagCompound containing the NBTData for the Outputs
     */
    default void deserializeOutputs(NBTTagCompound nbt) {
        //Outputs
        final ItemStack[] outputStacks;
        final double[] chances;
        final NBTMatchType[] nbtMatchOutput;
        
        int size;
        //Load OutputItemStacks
        NBTTagList outItemList = nbt.getTagList(NBTKeys.ICRAFT_RECIPE_STACKS_OUT, Constants.NBT.TAG_COMPOUND);
        outputStacks = new ItemStack[outItemList.tagCount()];
        resizeOutputCount(outputStacks.length);
        Arrays.setAll(outputStacks, i -> {
            ItemStack stack = new ItemStack(outItemList.getCompoundTagAt(i));
            return stack.isEmpty() ? ItemStack.EMPTY : stack;
        });
        
        chances = new double[outputStacks.length];
        nbtMatchOutput = new NBTMatchType[outputStacks.length];
        
        //Load the chance of each OutputItemStack or 1
        NBTTagList chanceTagList = nbt.getTagList(NBTKeys
                .ICRAFT_RECIPE_CHANCE_OUT, Constants.NBT.TAG_BYTE);
        size = Math.min(chanceTagList.tagCount(), outputStacks.length);
        for (int i = 0; i < size; i++) {
            chances[i] = chanceTagList.getDoubleAt(i);
        }
        for (int i = size; i < outputStacks.length; i++) {
            chances[i] = 1;
        }
        
        //Load NBTMatchType for InputItemStacks if missing default to FULL
        NBTTagList nbtMatchListOut = nbt.getTagList(NBTKeys.ICRAFT_RECIPE_NBT_OUT, Constants.NBT.TAG_COMPOUND);
        size = Math.min(nbtMatchListOut.tagCount(), outputStacks.length);
        for (int i = 0; i < size; i++) {
            int cur = nbtMatchListOut.getIntAt(i);
            if (cur < NBTMatchType.values().length && cur >= 0) {
                nbtMatchOutput[i] = NBTMatchType.values()[i];
            } else {
                nbtMatchOutput[i] = NBTMatchType.FULL;
            }
        }
        for (int i = size; i < outputStacks.length; i++) {
            nbtMatchOutput[i] = NBTMatchType.FULL;
        }
        
        for (int i = 0; i < outputStacks.length; i++) {
            setOutput(i, outputStacks[i], chances[i], nbtMatchOutput[i]);
        }
        
    }
    
    /**
     * Used to spilt the deserialization into smaller chunks
     * this one handles the deserialization of all the state that has to do with the Inputs of this Recipe
     *
     * @param nbt the NBTTagCompound containing the NBTData for the Inputs
     */
    default void deserializeInputs(NBTTagCompound nbt) {
        //Inputs
        
        final ItemStack[] inputStacks;
        final boolean[] oreDict;
        final NBTMatchType[] nbtMatchInput;
        
        //Load InputItemStacks
        NBTTagList inItemList = nbt.getTagList(NBTKeys.ICRAFT_RECIPE_STACKS_IN, Constants.NBT.TAG_COMPOUND);
        inputStacks = new ItemStack[inItemList.tagCount()];
        resizeInputCount(inputStacks.length);
        Arrays.setAll(inputStacks, i -> {
            ItemStack stack = new ItemStack(inItemList.getCompoundTagAt(i));
            return stack.isEmpty() ? ItemStack.EMPTY : stack;
        });
        
        oreDict = new boolean[inputStacks.length];
        nbtMatchInput = new NBTMatchType[inputStacks.length];
        
        //Load if InputItemStacks are OreDict if missing default to false (as boolean default to false nothing needs to be done in that case)
        NBTTagList ored = nbt.getTagList(NBTKeys
                .ICRAFT_RECIPE_OREDICT_LIST, Constants.NBT.TAG_BYTE);
        int size = Math.min(ored.tagCount(), inputStacks.length);
        for (int i = 0; i < size; i++) {
            oreDict[i] = ((NBTTagByte) ored.get(i)).getByte() != 0;
        }
        
        //Load NBTMatchType for InputItemStacks if missing default to FULL
        NBTTagList nbtMatchListIn = nbt.getTagList(NBTKeys.ICRAFT_RECIPE_NBT_IN, Constants.NBT.TAG_COMPOUND);
        size = Math.min(nbtMatchListIn.tagCount(), inputStacks.length);
        for (int i = 0; i < size; i++) {
            int cur = nbtMatchListIn.getIntAt(i);
            if (cur < NBTMatchType.values().length && cur >= 0) {
                nbtMatchInput[i] = NBTMatchType.values()[i];
            } else {
                nbtMatchInput[i] = NBTMatchType.FULL;
            }
        }
        for (int i = size; i < nbtMatchInput.length; i++) {
            nbtMatchInput[i] = NBTMatchType.FULL;
        }
        
        for (int i = 0; i < inputStacks.length; i++) {
            setInput(i, inputStacks[i], oreDict[i], nbtMatchInput[i]);
        }
        
    }
}
