package de.webtwob.mbma.common.references;

import net.minecraft.util.ResourceLocation;

import static de.webtwob.mbma.MultiblockMaschineAutomation.MODID;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class MBMAResources {

    //CapabilityProvider
    public static final ResourceLocation CAP_BLOCK_POSITION = new ResourceLocation(MODID, "blockPos");
    public static final ResourceLocation CAP_CRAFTING_REQUEST = new ResourceLocation(MODID, "craftingRequest");
    public static final ResourceLocation CAP_CRAFTING_RECIPE = new ResourceLocation(MODID,"craftingRecipe");

    //Textures
    public static final ResourceLocation QUEUESTACK_GUI = new ResourceLocation(MODID, "textures/gui/queuestack.png");
    public static final ResourceLocation TOKEN_GUI = new ResourceLocation(MODID, "textures/gui/token.png");
    public static final ResourceLocation RECIPE_BANK_GUI = new ResourceLocation(MODID,"textures/gui/recipe_bank.png");
    public static final ResourceLocation TOKEN_GENERATOR_GUI = new ResourceLocation(MODID,"textures/gui/token_generator.png");

    //Blocks
    public static final ResourceLocation INTERFACE_REGISTRY_NAME = new ResourceLocation(MODID, "storageInterface");
    public static final ResourceLocation TOKEN_GENERATOR_REGISTRY_NAME = new ResourceLocation(MODID,"tokengenerator");
    public static final ResourceLocation QUEUESTACK_REGISTRY_NAME = new ResourceLocation(MODID, "queuestack");
    public static final ResourceLocation RECIPE_BANK_REGISTRY_NAME = new ResourceLocation(MODID,"recipeBank");

    //Items
    public static final ResourceLocation TOKEN_REGISTRY_NAME = new ResourceLocation(MODID, "token");
    public static final ResourceLocation LINKCARD_REGISTRY_NAME = new ResourceLocation(MODID, "linkcard");
    public static final ResourceLocation RECIPE_PATTERN_REGISTRY_NAME = new ResourceLocation(MODID, "recipe_pattern");
    public static final ResourceLocation LINKED = new ResourceLocation(MODID,"islinked");

}
