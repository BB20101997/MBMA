package de.webtwob.mbma.api.capability.factory;

import de.webtwob.mbma.api.interfaces.capability.ICraftingRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by bennet on 03.04.17.
 */
public class CraftingRecipeFactory implements Callable<ICraftingRecipe> {

    @Override
    public ICraftingRecipe call() {
        return new ICraftingRecipe() {
            private NonNullList<InputTuple> inputTuples = NonNullList.create();
            private NonNullList<OutputTuple> outputTuples = NonNullList.create();
            private boolean vanilla;

            @Override
            public boolean isVanilla() {
                return vanilla;
            }

            @Override
            public void setVanilla(boolean vanilla) {
                this.vanilla = vanilla;
            }

            @Override
            public List<InputTuple> getInputs() {
                return inputTuples;
            }

            @Override
            public List<OutputTuple> getOutputs() {
                return outputTuples;
            }

            @Override
            public void addInput(final InputTuple input) {
                inputTuples.add(input);
            }

            @Override
            public void addOutput(final OutputTuple output) {
                outputTuples.add(output);
            }

            @Override
            public void removeInput(final InputTuple input) {
                inputTuples.remove(input);
            }

            @Override
            public void removeOutput(final OutputTuple output) {
                outputTuples.remove(output);
            }

            @Override
            public NBTTagCompound serializeNBT() {
                NBTTagCompound compound = new NBTTagCompound();
                NBTTagList list = new NBTTagList();
                for (InputTuple inputTuple : getInputs()) {
                    list.appendTag(inputTuple.serializeNBT());
                }
                compound.setTag(NBTINPUTS, list);
                list = new NBTTagList();
                for (OutputTuple outputTuple : outputTuples) {
                    list.appendTag(outputTuple.serializeNBT());
                }
                compound.setTag(NBTOUTPUTS, list);
                return compound;
            }

            @Override
            public void deserializeNBT(final NBTTagCompound compound) {
                if (compound == null) return;
                NBTTagList list = compound.getTagList(NBTINPUTS, Constants.NBT.TAG_COMPOUND);
                InputTuple input;
                for (int i = 0; i < list.tagCount(); i++) {
                    input = getInputTuple();
                    input.deserializeNBT(list.getCompoundTagAt(i));
                    addInput(input);
                }
                list = compound.getTagList(NBTINPUTS, Constants.NBT.TAG_COMPOUND);
                OutputTuple output;
                for (int i = 0; i < list.tagCount(); i++) {
                    output = getOutputTuple();
                    output.deserializeNBT(list.getCompoundTagAt(i));
                    addOutput(output);
                }
            }
        };
    }

    private ICraftingRecipe.OutputTuple getOutputTuple() {
        return new ICraftingRecipe.OutputTuple() {

            boolean guarantee;
            ItemStack result = ItemStack.EMPTY;

            @Override
            public boolean isGuaranteed() {
                return guarantee;
            }

            @Override
            public void setGuaranteed(final boolean bool) {
                guarantee = bool;
            }

            @Override
            public ItemStack getResult() {
                return result;
            }

            @Override
            public void setResult(final ItemStack stack) {
                if (stack == null) {
                    this.result = ItemStack.EMPTY;
                } else {
                    this.result = stack;
                }
            }
        };
    }

    private ICraftingRecipe.InputTuple getInputTuple() {
        return new ICraftingRecipe.InputTuple() {
            private int slot;
            private ItemStack stack = ItemStack.EMPTY;

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public void setSlot(final int i) {
                slot = i;
            }

            @Override
            public ItemStack getItemStack() {
                return stack;
            }

            @Override
            public void setItemStack(final ItemStack stack) {
                if (stack == null) {
                    this.stack = ItemStack.EMPTY;
                } else {
                    this.stack = stack;
                }
            }
        };
    }
}
