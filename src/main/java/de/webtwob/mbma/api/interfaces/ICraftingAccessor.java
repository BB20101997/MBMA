package de.webtwob.mbma.api.interfaces;

import de.webtwob.mbma.api.RecipePage;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by BB20101997 on 22. Apr. 2017.
 */
public interface ICraftingAccessor {
    
    /**
     * Takes an ItemStack to store in a secured location
     * where it can be retrieved once all crafting components are gathered
     * @return an id for the stack so that it can be retrieved later
     * */
    int putItemInStorage(@Nonnull ItemStack stack);
    
    /**
     * @return the ItemStack that was assigned this id when it was put or null
     * if no Stack currently has that id
     * */
    ItemStack retrieveItemFromStorage(int id);
    
    /**
     * Push an ItemStack out to reduce the amount left to craft,
     * should update the request
     * */
    void pushResult(int id);
    
    /**
     * Push excess back into the main Storage
     * */
    void dropExcess(int id);
    
    /**
     * In case of an unrecoverable error this can be called to return all collected Items to the main Storage
     * */
    void dropStorage();
    
    /**
     * @return a copy of the ItemStack with id to use for calculations etc.
     * */
    ItemStack peekAtItemInStorage(int id);
    
    List<RecipePage> getRecipePages();
    
    /**
     * @return the Storage id's for the ItemStacks
     * */
    int[] gatherMatchingStacks(Predicate<ItemStack> filter, int amount);
    
    
    
}
