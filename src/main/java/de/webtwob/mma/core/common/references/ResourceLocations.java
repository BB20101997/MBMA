package de.webtwob.mma.core.common.references;

import net.minecraft.util.ResourceLocation;

import static de.webtwob.mma.core.MMACore.MODID;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public class ResourceLocations {

    public static class MultiBlockGroups{
        public static final ResourceLocation MBGM_STORAGE = new ResourceLocation(MODID, "storage");
        public static final ResourceLocation MBGM_CRAFTING = new ResourceLocation(MODID, "crafting");
        public static final ResourceLocation MBGM_RECIPE = new ResourceLocation(MODID, "recipes");
        public static final ResourceLocation MBGM_QUEUE = new ResourceLocation(MODID, "queue");

        private MultiBlockGroups() {
        }
    }

    public static class Textures{
        public static final ResourceLocation QUEUES_GUI = new ResourceLocation(MODID, "textures/gui/queue_interface.png");
        public static final ResourceLocation TOKEN_GUI = new ResourceLocation(MODID, "textures/gui/token.png");
        public static final ResourceLocation RECIPE_BANK_GUI = new ResourceLocation(MODID, "textures/gui/recipe_bank.png");
        public static final ResourceLocation TOKEN_GENERATOR_GUI = new ResourceLocation(MODID, "textures/gui/token_generator.png");
        public static final ResourceLocation LINKING_INTERFACE = new ResourceLocation(MODID,"textures/gui/linking_interface.png");
    
        private Textures() {
        }
    }

    public static class Blocks {
        public static final ResourceLocation TOKEN_GENERATOR_REGISTRY_NAME = new ResourceLocation(MODID, "tokengenerator");

        public static final ResourceLocation PATTERN_STORAGE_REGISTRY_NAME = new ResourceLocation(MODID, "pattern_storage");

        public static final ResourceLocation CRAFTING_CONTROLLER_RL = new ResourceLocation(MODID, "crafting_controller");
        public static final ResourceLocation CRAFTING_PROCESSOR_RL = new ResourceLocation(MODID, "crafting_processor");
        public static final ResourceLocation CRAFTING_STORAGE_RL = new ResourceLocation(MODID, "crafting_storage");

        public static final ResourceLocation STORAGE_INTERFACE_RL = new ResourceLocation(MODID, "storage_interface");
        public static final ResourceLocation STORAGE_INDEXER_RL = new ResourceLocation(MODID, "storage_indexer");
        public static final ResourceLocation QUEUE_RL = new ResourceLocation(MODID,"queue");

        private Blocks() {
        }
    }

    public static class Items {
        public static final ResourceLocation TOKEN_REGISTRY_NAME = new ResourceLocation(MODID, "token");
        public static final ResourceLocation LINKCARD_REGISTRY_NAME = new ResourceLocation(MODID, "linkcard");
        public static final ResourceLocation RECIPE_PATTERN_REGISTRY_NAME = new ResourceLocation(MODID, "recipe_pattern");
        public static final ResourceLocation LINKED = new ResourceLocation(MODID, "islinked");
        public static final ResourceLocation DEBUG_WAND_REGISTRY_NAME = new ResourceLocation(MODID, "debugWand");
        public static final ResourceLocation IN_WORLD_CRAFTER_REGISTRY_NAME = new ResourceLocation(MODID,"inWorldCrafter");
    
        private Items() {
        }
    }

    public static class Capabilities {
        //CapabilityProvider
        public static final ResourceLocation CAP_BLOCK_POSITION = new ResourceLocation(MODID, "blockPos");
        public static final ResourceLocation CAP_CRAFTING_REQUEST = new ResourceLocation(MODID, "craftingRequest");
        public static final ResourceLocation CAP_CRAFTING_RECIPE = new ResourceLocation(MODID, "craftingRecipe");

        private Capabilities() {
        }
    }

    private ResourceLocations(){}

}
