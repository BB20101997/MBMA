package de.webtwob.mbma.api.capability.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bennet on 03.04.17.
 */
public interface ICraftingRecipe extends INBTSerializable<NBTTagCompound>{
    String NBTINPUTS = "mbma:inputs";
    String NBTOUTPUTS = "mbma:outputs";
    String NBTVANILLA = "mbma:vanilla";
    
    boolean isVanilla();
    void setVanilla(boolean vanilla);
    
    List<InputTuple> getInputs();
    List<OutputTuple> getOutputs();
    
    void addInput(InputTuple input);
    void addOutput(OutputTuple output);
    void removeInput(InputTuple input);
    void removeOutput(OutputTuple output);
    
    default List<ItemStack> getRequirements() {
        return getInputs().stream().map(InputTuple::getItemStack).collect(Collectors.toList());
    }
    
    default List<ItemStack> getResults(){
        return getOutputs().stream().map(OutputTuple::getResult).collect(Collectors.toList());
    }
    
    interface  OutputTuple extends INBTSerializable<NBTTagCompound>{
            String NBTSTACK = "mbma:stack";
            String NBTGUARANTEE = "mbma:guarantee";
            
            
            void setGuaranteed(boolean bool);
            boolean isGuaranteed();
            ItemStack getResult();
            void setResult(ItemStack stack);
    
        @Override
        default NBTTagCompound serializeNBT(){
            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean(NBTGUARANTEE,isGuaranteed());
            compound.setTag(NBTSTACK,getResult().serializeNBT());
            return compound;
        }
    
        @Override
        default void deserializeNBT(NBTTagCompound compound){
            if(compound==null)compound=new NBTTagCompound();
            setGuaranteed(compound.getBoolean(NBTGUARANTEE));
            setResult(new ItemStack(compound.getCompoundTag(NBTSTACK)));
        }
    }
    
    interface InputTuple extends INBTSerializable<NBTTagCompound> {
        
        String NBTSLOT  = "mbma:slot";
        String NBTSTACK = "mbma:stack";
        
        int getSlot();
        
        void setSlot(int i);
        
        ItemStack getItemStack();
        
        void setItemStack(ItemStack stack);
        
        @Override
        default NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger(NBTSLOT, getSlot());
            compound.setTag(NBTSTACK, getItemStack().serializeNBT());
            return compound;
        }
        
        @Override
        default void deserializeNBT(NBTTagCompound nbtTagCompound) {
            if(nbtTagCompound == null) {
                nbtTagCompound = new NBTTagCompound();
            }
            setSlot(nbtTagCompound.getInteger(NBTSLOT));
            setItemStack(new ItemStack(nbtTagCompound.getCompoundTag(NBTSTACK)));
        }
    }
}
