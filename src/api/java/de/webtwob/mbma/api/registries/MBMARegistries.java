package de.webtwob.mbma.api.registries;

import de.webtwob.mbma.api.references.ResourceLocations;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmaapi")
public class MBMARegistries {
    
    private MBMARegistries() {
    }
    
    @SubscribeEvent
    public static void registerRegistries(RegistryEvent.NewRegistry event) {
        new RegistryBuilder<MultiBlockGroupType>()
                .setName(ResourceLocations.REG_MULTIBLOCK)
                .setType(MultiBlockGroupType.class)
                .set(
                        key -> {
                            MultiBlockGroupType multiBlockGroupType = new MultiBlockGroupType();
                            multiBlockGroupType.setRegistryName(key);
                            return multiBlockGroupType;
                        })
                .allowModification()
                .disableSaving()
                .create();
        
        new RegistryBuilder<RecipeType>()
                .setName(ResourceLocations.REG_RECIPE_TYPE)
                .setType(RecipeType.class).set(
                key -> {
                    RecipeType type = new RecipeType.CustomtRecipeType();
                    type.setRegistryName(key);
                    return type;
                }).setDefaultKey(ResourceLocations.REG_RECIPE_CUSTOM)
                .allowModification()
                .disableSaving()
                .create();
    }
    
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<RecipeType> registryEvent){
        IForgeRegistry<RecipeType> registry = registryEvent.getRegistry();
        
        RecipeType custom = new RecipeType.CustomtRecipeType();
        custom.setRegistryName(ResourceLocations.REG_RECIPE_CUSTOM);
        registry.register(custom);
        
        RecipeType vanilla = new RecipeType.VanillaCraftingRecipeType();
        vanilla.setRegistryName(ResourceLocations.REG_RECIPE_VANILLA);
    }
}
