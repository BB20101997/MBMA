package de.webtwob.mma.core.common.registration;

import de.webtwob.mma.api.capability.provider.BlockPosProvider;
import de.webtwob.mma.api.capability.provider.CraftingRecipeProvider;
import de.webtwob.mma.api.capability.provider.CraftingRequestProvider;
import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.item.*;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

import static de.webtwob.mma.core.common.references.ResourceLocations.Capabilities.*;
import static de.webtwob.mma.core.common.references.ResourceLocations.Items.*;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
@Mod.EventBusSubscriber(modid = "mmacore")
public class Items {
    
    //ItemBlocks
    public static final ItemBlock STORAGE_INTERFACE_ITEM = new MMAItemBlock(Blocks.STORAGE_INTERFACE);
    public static final ItemBlock STORAGE_INDEXER_ITEM = new MMAItemBlock(Blocks.STORAGE_INDEXER);
    
    public static final ItemBlock TOKEN_GENERATOR_ITEM = new MMAItemBlock(Blocks.TOKEN_GENERATOR);
    public static final ItemBlock QUEUE_ITEM = new MMAItemBlock(Blocks.QUEUE);
    public static final ItemBlock PATTERN_STORAGE_ITEM = new MMAItemBlock(Blocks.PATTERN_STORAGE);
    
    public static final ItemBlock CRAFTING_STORAGE_ITEM = new MMAItemBlock(Blocks.CRAFTING_STORAGE);
    public static final ItemBlock CRAFTING_PROCESSOR_ITEM = new MMAItemBlock(Blocks.CRAFTING_PROCESSOR);
    public static final ItemBlock CRAFTING_CONTROLLER_ITEM = new MMAItemBlock(Blocks.CRAFTING_CONTROLLER);
    
    //Items
    public static final Item LINKCARD = new LinkCardItem(LINKCARD_REGISTRY_NAME);
    public static final Item TOKEN = new Token(TOKEN_REGISTRY_NAME);
    public static final Item RECIPE_PATTERN = new RecipePattern(RECIPE_PATTERN_REGISTRY_NAME);
    public static final Item DEBUG_WAND = new DebugWand(DEBUG_WAND_REGISTRY_NAME);
    public static final Item IN_WORLD_CRAFTER = new InWorldCrafterItem(IN_WORLD_CRAFTER_REGISTRY_NAME);
    
    private static final Item[] ITEMS = {//NOSONAR
            LINKCARD,
            TOKEN,
            RECIPE_PATTERN,
            DEBUG_WAND,
            IN_WORLD_CRAFTER
    };
    
    private static final ItemBlock[] ITEM_BLOCKS = {
            STORAGE_INTERFACE_ITEM,
            STORAGE_INDEXER_ITEM,
            TOKEN_GENERATOR_ITEM,
            QUEUE_ITEM,
            PATTERN_STORAGE_ITEM,
            CRAFTING_STORAGE_ITEM,
            CRAFTING_PROCESSOR_ITEM,
            CRAFTING_CONTROLLER_ITEM
    };
    
    private Items() {
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        CoreLog.info("Registering Items");
        
        IForgeRegistry<Item> registry = event.getRegistry();
        
        registry.registerAll(ITEMS);
        registry.registerAll(ITEM_BLOCKS);
    }
    
    @SubscribeEvent
    public static void attacheCapabilityProvider(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() == LINKCARD) {
            event.addCapability(CAP_BLOCK_POSITION, new BlockPosProvider(event.getObject()));
        }
        if (event.getObject().getItem() == TOKEN) {
            event.addCapability(CAP_CRAFTING_REQUEST, new CraftingRequestProvider(event.getObject()));
        }
        if (event.getObject().getItem() == RECIPE_PATTERN) {
            event.addCapability(CAP_CRAFTING_RECIPE, new CraftingRecipeProvider(event.getObject()));
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void initModels(ModelRegistryEvent event) {
        CoreLog.info("Registering Models");
        
        for (Item item : ITEMS) {
            registerDefaultModel(item);
        }
        for (ItemBlock itemBlock : ITEM_BLOCKS) {
            registerDefaultModel(itemBlock);
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static void registerDefaultModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
    }
}
