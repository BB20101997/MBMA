package de.webtwob.mbma.api.interfaces;


import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

/**
 * Created by BB20101997 on 22. Apr. 2017.
 */
@Deprecated
public interface ICraftingAccessor {

    /**
     * moves the stack to Temp
     */
    int stackToTemp(@Nonnull ItemStack stack);

    /**
     * empties Temp
     */
    void dropTemp();

    /**
     * @return a copy of the ItemStack with the id
     */
    ItemStack peekAtItemInTemp(int id);

    //List<RecipePage> getRecipePages();

    /**
     * @return an array of ItemStack ids
     */
    int[] gatherMatchingStacks(Predicate<ItemStack> filter, int amount);

    void returnStackToPerm(int stackID);

}
