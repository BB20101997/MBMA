package de.webtwob.mbma.common.references;

import net.minecraft.util.ResourceLocation;

import static de.webtwob.mbma.MultiblockMaschineAutomation.MODID;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class MBMAResources {

    //CapabilityProvider
    public static final ResourceLocation CAP_BLOCK_POSITION   = new ResourceLocation(MODID, "blockPos");
    public static final ResourceLocation CAP_CRAFTING_REQUEST = new ResourceLocation(MODID, "craftingRequest");

    //Textures
    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(MODID, "textures/gui/queuestack.png");

    //Blocks
    public static final ResourceLocation PSI_REGISTRY_NAME        = new ResourceLocation(MODID, "psi");
    public static final ResourceLocation QUEUESTACK_REGISTRY_NAME = new ResourceLocation(MODID, "queuestack");

    //Items
    public static final ResourceLocation TOKEN_REGISTRY_NAME          = new ResourceLocation(MODID, "token");
    public static final ResourceLocation LINKCARD_REGISTRY_NAME       = new ResourceLocation(MODID, "linkcard");
    public static final ResourceLocation RECIPE_PATTERN_REGISTRY_NAME = new ResourceLocation(MODID, "recipe_pattern");
}
