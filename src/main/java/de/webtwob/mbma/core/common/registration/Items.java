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
import net.minecraft.util.ResourceLocation;
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

import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.Blocks.*;
import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.Capabilities.*;
import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.Items.*;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmacore")
public class Items {
    
    //ItemBlocks
    public static final ItemBlock STORAGE_INTERFACE_ITEM = new ItemBlock(Blocks.STORAGE_INTERFACE);
    public static final ItemBlock STORAGE_INDEXER_ITEM = new ItemBlock(Blocks.STORAGE_INDEXER);
    
    public static final ItemBlock TOKEN_GENERATOR_ITEM = new ItemBlock(Blocks.TOKEN_GENERATOR);
    public static final ItemBlock QUEUE_ITEM = new ItemBlock(Blocks.QUEUE);
    
    public static final ItemBlock CRAFTING_STORAGE_ITEM = new ItemBlock(Blocks.CRAFTING_STORAGE);
    public static final ItemBlock CRAFTING_PROCESSOR_ITEM = new ItemBlock(Blocks.CRAFTING_PROCESSOR);
    public static final ItemBlock CRAFTING_CONTROLLER_ITEM = new ItemBlock(Blocks.CRAFTING_CONTROLLER);
    
    //Items
    public static final Item LINKCARD = new LinkCardItem();
    public static final Item TOKEN = new Token();
    public static final Item RECIPE_PATTERN = new RecipePattern();
    public static final Item DEBUG_WAND = new DebugWand();
    
    private Items() {
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        MBMALog.info("Registering Items");
    
        IForgeRegistry<Item> registry = event.getRegistry();
    
        //set Register names
        setNameAndRegister(LINKCARD,LINKCARD_REGISTRY_NAME,registry);
        setNameAndRegister(TOKEN,TOKEN_REGISTRY_NAME,registry);
        setNameAndRegister(RECIPE_PATTERN,RECIPE_PATTERN_REGISTRY_NAME,registry);
        setNameAndRegister(DEBUG_WAND,DEBUG_WAND_REGISTRY_NAME,registry);

        setNameAndRegister(STORAGE_INTERFACE_ITEM,STORAGE_INTERFACE_RL,registry);
        setNameAndRegister(STORAGE_INDEXER_ITEM,STORAGE_INDEXER_RL,registry);

        setNameAndRegister(TOKEN_GENERATOR_ITEM,TOKEN_GENERATOR_REGISTRY_NAME,registry);
        setNameAndRegister(QUEUE_ITEM,QUEUE_RL,registry);
    
        setNameAndRegister(CRAFTING_CONTROLLER_ITEM,CRAFTING_CONTROLLER_RL,registry);
        setNameAndRegister(CRAFTING_PROCESSOR_ITEM,CRAFTING_PROCESSOR_RL,registry);
        setNameAndRegister(CRAFTING_STORAGE_ITEM,CRAFTING_STORAGE_RL,registry);
    }
    
    private static void setNameAndRegister(Item item, ResourceLocation resourceLocation, IForgeRegistry<Item> reg) {
        item.setRegistryName(resourceLocation);
        item.setUnlocalizedName(resourceLocation.toString());
        reg.register(item);
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
        registerDefaultModel(STORAGE_INTERFACE_ITEM);
        registerDefaultModel(STORAGE_INDEXER_ITEM);
        
        registerDefaultModel(TOKEN_GENERATOR_ITEM);
        registerDefaultModel(QUEUE_ITEM);

        registerDefaultModel(CRAFTING_STORAGE_ITEM);
        registerDefaultModel(CRAFTING_PROCESSOR_ITEM);
        registerDefaultModel(CRAFTING_CONTROLLER_ITEM);
        
        //Items
        registerDefaultModel(LINKCARD);
        registerDefaultModel(TOKEN);
        registerDefaultModel(RECIPE_PATTERN);
        registerDefaultModel(DEBUG_WAND);
    }
    
    @SideOnly(Side.CLIENT)
    private static void registerDefaultModel(Item item){
        ModelLoader.setCustomModelResourceLocation(item,0,new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()),"inventory"));
    }
}
