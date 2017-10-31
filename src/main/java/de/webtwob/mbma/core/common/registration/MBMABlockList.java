package de.webtwob.mbma.core.common.registration;

import de.webtwob.mbma.core.common.MBMALog;
import de.webtwob.mbma.core.common.block.*;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.Blocks.*;


/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmacore")
public class MBMABlockList {

    
    public static final BlockStorageInterface STORAGE_INTERFACE = new BlockStorageInterface();
    public static final BlockStorageIndexer STORAGE_INDEXER = new BlockStorageIndexer();
    
    public static final BlockRequestGenerator TOKEN_GENERATOR = new BlockRequestGenerator();
    
    public static final BlockCraftingController CRAFTING_CONTROLLER = new BlockCraftingController();
    public static final BlockCraftingProcessor CRAFTING_PROCESSOR = new BlockCraftingProcessor();
    public static final BlockCraftingStorage   CRAFTING_STORAGE = new BlockCraftingStorage();
    
    private MBMABlockList() {
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        MBMALog.info("Registering Blocks");
    
        setName(STORAGE_INTERFACE, STORAGE_INTERFACE_RL);
        setName(STORAGE_INDEXER, STORAGE_INDEXER_RL);
        
        setName(TOKEN_GENERATOR,TOKEN_GENERATOR_REGISTRY_NAME);
        
        setName(CRAFTING_CONTROLLER, CRAFTING_CONTROLLER_RL);
        setName(CRAFTING_PROCESSOR, CRAFTING_PROCESSOR_RL);
        setName(CRAFTING_STORAGE, CRAFTING_STORAGE_RL);
    
        IForgeRegistry<Block> registry = event.getRegistry();
        
        //register blocks
        registry.registerAll(STORAGE_INTERFACE, STORAGE_INDEXER,TOKEN_GENERATOR);
        registry.registerAll(CRAFTING_CONTROLLER,CRAFTING_PROCESSOR,CRAFTING_STORAGE);
    }
    
    private static void setName(Block block, ResourceLocation rs){
        block.setRegistryName(rs);
        block.setUnlocalizedName(rs.toString());
    }
}
