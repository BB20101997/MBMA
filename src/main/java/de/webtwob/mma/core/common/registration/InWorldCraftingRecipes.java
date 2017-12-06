package de.webtwob.mma.core.common.registration;


import de.webtwob.mma.api.registries.InWorldRecipe;
import de.webtwob.mma.core.MMACore;
import de.webtwob.mma.core.common.CoreLog;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static de.webtwob.mma.core.common.registration.world.recipes.DefaultRecipes.CRAFTING_PROCESSOR_RECIPE;
import static de.webtwob.mma.core.common.registration.world.recipes.DefaultRecipes.CRAFTING_STORAGE_RECIPE;

/**
 * Created by BB20101997 on 04. Dez. 2017.
 */
@Mod.EventBusSubscriber(modid = MMACore.MODID)
public class InWorldCraftingRecipes {
    
    private InWorldCraftingRecipes() {
    }
    
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<InWorldRecipe> event) {
        CoreLog.info("Registering InWorldRecipes");
        
        CRAFTING_STORAGE_RECIPE.setRegistryName(new ResourceLocation(MMACore.MODID, "iwr.crafting.storage"));
        CRAFTING_PROCESSOR_RECIPE.setRegistryName(new ResourceLocation(MMACore.MODID,"iwr.crafting.processor"));
        
        event.getRegistry().registerAll(CRAFTING_STORAGE_RECIPE,CRAFTING_PROCESSOR_RECIPE);
    }
}
