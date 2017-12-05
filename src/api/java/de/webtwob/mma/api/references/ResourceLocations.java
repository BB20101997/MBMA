package de.webtwob.mma.api.references;

import net.minecraft.util.ResourceLocation;

import static de.webtwob.mma.api.MMAAPI.MODID;

/**
 * Created by BB20101997 on 12. Nov. 2017.
 */
public class ResourceLocations {

    //Registrie ResourceLocations
    public static final ResourceLocation REG_MULTIBLOCK = new ResourceLocation(MODID, "multiblock");
    public static final ResourceLocation REG_RECIPE_TYPE = new ResourceLocation(MODID, "recipeType");

    public static final ResourceLocation REG_RECIPE_VANILLA = new ResourceLocation(MODID,"vanillaRecipe");
    public static final ResourceLocation REG_RECIPE_CUSTOM = new ResourceLocation(MODID,"customRecipe");
    public static final ResourceLocation REG_IN_WORLD_RECIPE = new ResourceLocation(MODID,"inWorldRecipe");
    
    private ResourceLocations() {
    }
}
