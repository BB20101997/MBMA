package de.webtwob.mma.api.registries;

import de.webtwob.mma.api.APILog;
import de.webtwob.mma.api.references.ResourceLocations;

import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@Mod.EventBusSubscriber(modid = "mmaapi")
public class MMARegistries {
    
    private MMARegistries() {
    }
    
    /**
     * @param event the registry Event indicating that it's time to Register all Registries
     */
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
                    RecipeType type = new RecipeType.CustomRecipeType();
                    type.setRegistryName(key);
                    return type;
                }).setDefaultKey(ResourceLocations.REG_RECIPE_CUSTOM)
                .allowModification()
                .disableSaving()
                .create();
        
        new RegistryBuilder<InWorldRecipe>().setName(ResourceLocations.REG_IN_WORLD_RECIPE)
                .setType(InWorldRecipe.class)
                .set(key ->
                        new InWorldRecipe() {
                            @Override
                            public void spawResultAt(World world, BlockPos pos, EnumFacing facing) {
                            
                            }
                            
                            @Override
                            public BlockPattern getBlockPattern() {
                                return FactoryBlockPattern.start().aisle("").build();
                            }
                            
                            @Override
                            public List<ItemStack> getResults(World world, BlockPos pos, EnumFacing facing) {
                                return new ArrayList<>();
                            }
                        }
                
                ).add((owner, stage, id, obj, oldObj) -> APILog.info("Adding InWorldRecipe:" + obj.getRegistryName()))
                .allowModification()
                .disableSaving()
                .create();
    }
    
    /**
     * @param registryEvent the event indicating and granting access to register all our RecipeTypes
     */
    @SubscribeEvent
    public static void registerRecipeTypes(RegistryEvent.Register<RecipeType> registryEvent) {
        IForgeRegistry<RecipeType> registry = registryEvent.getRegistry();
        
        RecipeType custom = new RecipeType.CustomRecipeType();
        custom.setRegistryName(ResourceLocations.REG_RECIPE_CUSTOM);
        registry.register(custom);
        
        RecipeType vanilla = new RecipeType.VanillaCraftingRecipeType();
        vanilla.setRegistryName(ResourceLocations.REG_RECIPE_VANILLA);
    }
}
