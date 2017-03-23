package de.webtwob.mbma.common.item;

import de.webtwob.mbma.MultiblockMaschineAutomation;
import de.webtwob.mbma.api.capability.provider.BlockPosProvider;
import de.webtwob.mbma.api.capability.provider.CraftingRecipeProvider;
import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.block.MBMABlockList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static de.webtwob.mbma.common.references.MBMAResources.*;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
@Mod.EventBusSubscriber
public class MBMAItemList {

    //ItemBlocks
    public static final ItemBlock STORAGE_INTERFACE_ITEM = new ItemBlock(MBMABlockList.STORAGE_INTERFACE_BLOCK);
    public static final ItemBlock QUEUE_STACK_ITEM = new ItemBlock(MBMABlockList.QUEUE_STACK_BLOCK);
    public static final ItemBlock TOKEN_GENERATOR_ITEM = new ItemBlock(MBMABlockList.TOKEN_GENERATOR_BLOCK);

    //Items
    public static final Item LINKCARD       = new LinkCardItem();
    public static final Item TOKEN          = new Token();
    public static final Item RECIPE_PATTERN = new RecipePattern();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        MBMALog.info("Registering Items");

        //set Register names
        STORAGE_INTERFACE_ITEM.setRegistryName(INTERFACE_REGISTRY_NAME);
        LINKCARD.setRegistryName(LINKCARD_REGISTRY_NAME);
        QUEUE_STACK_ITEM.setRegistryName(QUEUESTACK_REGISTRY_NAME);
        TOKEN.setRegistryName(TOKEN_REGISTRY_NAME);
        RECIPE_PATTERN.setRegistryName(RECIPE_PATTERN_REGISTRY_NAME);
        TOKEN_GENERATOR_ITEM.setRegistryName(TOKEN_GENERATOR_REGISTRY_NAME);

        //set Unlocalized names
        TOKEN.setUnlocalizedName(TOKEN_REGISTRY_NAME.toString());
        LINKCARD.setUnlocalizedName(LINKCARD_REGISTRY_NAME.toString());
        RECIPE_PATTERN.setUnlocalizedName(RECIPE_PATTERN_REGISTRY_NAME.toString());

        IForgeRegistry<Item> registry = event.getRegistry();

        //register items
        registry.registerAll(STORAGE_INTERFACE_ITEM, LINKCARD, QUEUE_STACK_ITEM, TOKEN, RECIPE_PATTERN,TOKEN_GENERATOR_ITEM);
        MultiblockMaschineAutomation.proxy.initModel();
    }

    @SubscribeEvent
    public static void attacheBlockPosProvider(AttachCapabilitiesEvent<Item> event) {
        if(event.getObject() == LINKCARD) {
            event.addCapability(CAP_BLOCK_POSITION, new BlockPosProvider());
        }
        if(event.getObject() == TOKEN){
            event.addCapability(CAP_CRAFTING_REQUEST, new CraftingRecipeProvider());
        }
    }


    @SideOnly(Side.CLIENT)
    public static void initModels() {
        //ItemBlocks
        ModelLoader.setCustomModelResourceLocation(STORAGE_INTERFACE_ITEM,0,new ModelResourceLocation(INTERFACE_REGISTRY_NAME,"inventory"));
        ModelLoader.setCustomModelResourceLocation(QUEUE_STACK_ITEM,0,new ModelResourceLocation(QUEUESTACK_REGISTRY_NAME,"inventory"));
        ModelLoader.setCustomModelResourceLocation(TOKEN_GENERATOR_ITEM,0,new ModelResourceLocation(TOKEN_GENERATOR_REGISTRY_NAME,"inventory"));

        //Items
        ModelLoader.setCustomModelResourceLocation(LINKCARD,0,new ModelResourceLocation(LINKCARD_REGISTRY_NAME,"inventory"));
        ModelLoader.setCustomModelResourceLocation(TOKEN,0,new ModelResourceLocation(TOKEN_REGISTRY_NAME,"inventory"));
        ModelLoader.setCustomModelResourceLocation(RECIPE_PATTERN,0,new ModelResourceLocation(RECIPE_PATTERN_REGISTRY_NAME,"inventory"));
    }
}
