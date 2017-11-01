package de.webtwob.mbma.core.common.registration;

import de.webtwob.mbma.api.capability.provider.BlockPosProvider;
import de.webtwob.mbma.api.capability.provider.CraftingRecipeProvider;
import de.webtwob.mbma.api.capability.provider.CraftingRequestProvider;
import de.webtwob.mbma.core.common.MBMALog;
import de.webtwob.mbma.core.common.item.DebugWand;
import de.webtwob.mbma.core.common.item.LinkCardItem;
import de.webtwob.mbma.core.common.item.RecipePattern;
import de.webtwob.mbma.core.common.item.Token;

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

import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.Blocks.*;
import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.Capabilities.*;
import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.Items.*;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmacore")
public class MBMAItemList {
    
    //ItemBlocks
    public static final ItemBlock STORAGE_INTERFACE_ITEM = new ItemBlock(MBMABlockList.STORAGE_INTERFACE);
    public static final ItemBlock TOKEN_GENERATOR_ITEM = new ItemBlock(MBMABlockList.TOKEN_GENERATOR);
   
    public static final ItemBlock CRAFTING_STORAGE_ITEM = new ItemBlock(MBMABlockList.CRAFTING_STORAGE);
    public static final ItemBlock CRAFTING_PROCESSOR_ITEM = new ItemBlock(MBMABlockList.CRAFTING_PROCESSOR);
    public static final ItemBlock CRAFTING_CONTROLLER_ITEM = new ItemBlock(MBMABlockList.CRAFTING_CONTROLLER);
    //Items
    public static final Item LINKCARD = new LinkCardItem();
    public static final Item TOKEN = new Token();
    public static final Item RECIPE_PATTERN = new RecipePattern();
    public static final Item DEBUG_WAND = new DebugWand();
    
    private MBMAItemList() {
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        MBMALog.info("Registering Items");
        
        //set Register names
        LINKCARD.setRegistryName(LINKCARD_REGISTRY_NAME);
        TOKEN.setRegistryName(TOKEN_REGISTRY_NAME);
        RECIPE_PATTERN.setRegistryName(RECIPE_PATTERN_REGISTRY_NAME);
        DEBUG_WAND.setRegistryName(DEBUG_WAND_REGISTRY_NAME);
        
        STORAGE_INTERFACE_ITEM.setRegistryName(STORAGE_INTERFACE_RL);
        TOKEN_GENERATOR_ITEM.setRegistryName(TOKEN_GENERATOR_REGISTRY_NAME);
        
        CRAFTING_CONTROLLER_ITEM.setRegistryName(CRAFTING_CONTROLLER_RL);
        CRAFTING_PROCESSOR_ITEM.setRegistryName(CRAFTING_PROCESSOR_RL);
        CRAFTING_STORAGE_ITEM.setRegistryName(CRAFTING_STORAGE_RL);
        
        //set Unlocalized names
        TOKEN.setUnlocalizedName(TOKEN_REGISTRY_NAME.toString());
        LINKCARD.setUnlocalizedName(LINKCARD_REGISTRY_NAME.toString());
        RECIPE_PATTERN.setUnlocalizedName(RECIPE_PATTERN_REGISTRY_NAME.toString());
        DEBUG_WAND.setUnlocalizedName(DEBUG_WAND_REGISTRY_NAME.toString());
        
        IForgeRegistry<Item> registry = event.getRegistry();
        
        //register items
        registry.registerAll(STORAGE_INTERFACE_ITEM, LINKCARD,TOKEN, RECIPE_PATTERN, DEBUG_WAND, TOKEN_GENERATOR_ITEM, CRAFTING_CONTROLLER_ITEM,CRAFTING_PROCESSOR_ITEM,CRAFTING_STORAGE_ITEM);
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
        MBMALog.info("Registering Models");
        //ItemBlocks
        ModelLoader.setCustomModelResourceLocation(STORAGE_INTERFACE_ITEM, 0, new ModelResourceLocation(STORAGE_INTERFACE_RL, "inventory"));
        ModelLoader.setCustomModelResourceLocation(TOKEN_GENERATOR_ITEM, 0, new ModelResourceLocation(TOKEN_GENERATOR_REGISTRY_NAME, "inventory"));
        
        ModelLoader.setCustomModelResourceLocation(CRAFTING_CONTROLLER_ITEM,0,new ModelResourceLocation(CRAFTING_CONTROLLER_RL,"inventory"));
        ModelLoader.setCustomModelResourceLocation(CRAFTING_PROCESSOR_ITEM,0,new ModelResourceLocation(CRAFTING_PROCESSOR_RL,"inventory"));
        ModelLoader.setCustomModelResourceLocation(CRAFTING_STORAGE_ITEM,0, new ModelResourceLocation(CRAFTING_STORAGE_RL,"inventory"));
        
        //Items
        ModelLoader.setCustomModelResourceLocation(LINKCARD, 0, new ModelResourceLocation(LINKCARD_REGISTRY_NAME, "inventory"));
        ModelLoader.setCustomModelResourceLocation(TOKEN, 0, new ModelResourceLocation(TOKEN_REGISTRY_NAME, "inventory"));
        ModelLoader.setCustomModelResourceLocation(RECIPE_PATTERN, 0, new ModelResourceLocation(RECIPE_PATTERN_REGISTRY_NAME, "inventory"));
        ModelLoader.setCustomModelResourceLocation(DEBUG_WAND, 0, new ModelResourceLocation(DEBUG_WAND_REGISTRY_NAME, "inventory"));
    }
}
