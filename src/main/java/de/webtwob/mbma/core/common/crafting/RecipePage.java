package de.webtwob.mbma.core.common.crafting;

import de.webtwob.mbma.core.common.references.MBMA_NBTKeys;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;

import static net.minecraftforge.common.util.Constants.NBT;

/**
 * Created by bennet on 02.04.17.
 */
public class RecipePage implements INBTSerializable<NBTTagCompound> {

    public NonNullList<ItemStack> recipes = NonNullList.withSize(42, ItemStack.EMPTY);
    public NonNullList<ItemStack> maschineLinks = NonNullList.withSize(18, ItemStack.EMPTY);

    public String name;

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound comp = new NBTTagCompound();
        if (name != null) {
            comp.setString(MBMA_NBTKeys.RB_PAGE_NAME, name);
        }
        int itemIndex = 0;
        NBTTagCompound rec = new NBTTagCompound();
        for (ItemStack stack : recipes) {
            rec.setTag(MBMA_NBTKeys.RB_PAGE_RECIPE + itemIndex++, stack.serializeNBT());
        }
        comp.setTag(MBMA_NBTKeys.RB_PAGE_RECIPE + "s", rec);
        rec = new NBTTagCompound();
        itemIndex = 0;
        for (ItemStack stack : maschineLinks) {
            rec.setTag(MBMA_NBTKeys.RB_PAGE_LINK + itemIndex++, stack.serializeNBT());
        }
        comp.setTag(MBMA_NBTKeys.RB_PAGE_LINK + "s", rec);
        return comp;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        if (compound.hasKey(MBMA_NBTKeys.RB_PAGE_NAME)) {
            name = compound.getString(MBMA_NBTKeys.RB_PAGE_NAME);
        }
        NBTTagCompound rec;
        int index = 0;
        if (compound.hasKey(MBMA_NBTKeys.RB_PAGE_RECIPE + "s", NBT.TAG_COMPOUND)) {
            rec = compound.getCompoundTag(MBMA_NBTKeys.RB_PAGE_RECIPE + "s");
            for (String key : rec.getKeySet()) {
                recipes.set(index++, new ItemStack(rec.getCompoundTag(key)));
            }
        }
        index = 0;
        if (compound.hasKey(MBMA_NBTKeys.RB_PAGE_LINK + "s", NBT.TAG_COMPOUND)) {
            rec = compound.getCompoundTag(MBMA_NBTKeys.RB_PAGE_LINK + "s");
            for (String key : rec.getKeySet()) {
                maschineLinks.set(index++, new ItemStack(rec.getCompoundTag(key)));
            }
        }
    }

    public void setName(final String name) {
        this.name = name;
    }
}
