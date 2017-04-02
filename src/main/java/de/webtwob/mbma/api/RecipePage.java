package de.webtwob.mbma.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;

import static de.webtwob.mbma.common.references.MBMA_NBTKeys.RB_PAGE_LINK;
import static de.webtwob.mbma.common.references.MBMA_NBTKeys.RB_PAGE_NAME;
import static de.webtwob.mbma.common.references.MBMA_NBTKeys.RB_PAGE_RECIPE;

/**
 * Created by bennet on 02.04.17.
 */
public class RecipePage implements INBTSerializable<NBTTagCompound> {
    
    public NonNullList<ItemStack> recipes       = NonNullList.withSize(42, ItemStack.EMPTY);
    public NonNullList<ItemStack> maschineLinks = NonNullList.withSize(18, ItemStack.EMPTY);
    
    public String name;
    
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound comp = new NBTTagCompound();
        if(name != null) {
            comp.setString(RB_PAGE_NAME, name);
        }
        int            itemIndex = 0;
        NBTTagCompound rec       = new NBTTagCompound();
        for(ItemStack stack : recipes){
            rec.setTag(RB_PAGE_RECIPE + itemIndex++, stack.serializeNBT());
        }
        comp.setTag(RB_PAGE_RECIPE + "s", rec);
        rec = new NBTTagCompound();
        itemIndex = 0;
        for(ItemStack stack : maschineLinks){
            rec.setTag(RB_PAGE_LINK + itemIndex++, stack.serializeNBT());
        }
        comp.setTag(RB_PAGE_LINK + "s", rec);
        return comp;
    }
    
    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        if(compound.hasKey(RB_PAGE_NAME)) {
            name = compound.getString(RB_PAGE_NAME);
        }
        NBTTagCompound rec;
        if(compound.hasKey(RB_PAGE_RECIPE + "s")) {
            rec = compound.getCompoundTag(RB_PAGE_RECIPE + "s");
            for(String key : rec.getKeySet()){
                recipes.add(new ItemStack(rec.getCompoundTag(key)));
            }
        }
        if(compound.hasKey(RB_PAGE_LINK + "s")) {
            rec = compound.getCompoundTag(RB_PAGE_LINK + "s");
            for(String key : rec.getKeySet()){
                maschineLinks.add(new ItemStack(rec.getCompoundTag(key)));
            }
        }
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
