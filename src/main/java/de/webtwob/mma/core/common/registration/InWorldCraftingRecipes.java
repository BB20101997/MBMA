package de.webtwob.mma.core.common.registration;


import de.webtwob.mma.api.registries.InWorldRecipe;
import de.webtwob.mma.core.MMACore;
import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.registration.world.recipes.CraftingStorageRecipe;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by BB20101997 on 04. Dez. 2017.
 */
@Mod.EventBusSubscriber(modid = MMACore.MODID)
public class InWorldCraftingRecipes {
    
    public static final InWorldRecipe CRAFTING_STORAGE_RECIPE = new CraftingStorageRecipe();
    
    private InWorldCraftingRecipes() {
    }
    
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<InWorldRecipe> event) {
        CoreLog.info("Registering InWorldRecipes");
        
        CRAFTING_STORAGE_RECIPE.setRegistryName(new ResourceLocation(MMACore.MODID, "inworldrecipecraftingstorage"));
        
        event.getRegistry().registerAll(CRAFTING_STORAGE_RECIPE);
    }
}
