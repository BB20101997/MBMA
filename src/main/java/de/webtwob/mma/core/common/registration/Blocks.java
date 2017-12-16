package de.webtwob.mma.core.common.registration;

import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.block.*;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static de.webtwob.mma.core.common.references.ResourceLocations.Blocks.*;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber(modid = "mmacore")
public class Blocks {

    public static final BlockStorageInterface STORAGE_INTERFACE = new BlockStorageInterface(STORAGE_INTERFACE_RL);
    public static final BlockStorageIndexer   STORAGE_INDEXER   = new BlockStorageIndexer(STORAGE_INDEXER_RL);

    public static final BlockRequestGenerator TOKEN_GENERATOR = new BlockRequestGenerator(
            TOKEN_GENERATOR_REGISTRY_NAME);

    public static final BlockQueue QUEUE = new BlockQueue(QUEUE_RL);

    public static final BlockPatternStorage PATTERN_STORAGE = new BlockPatternStorage(PATTERN_STORAGE_REGISTRY_NAME);

    public static final BlockCraftingController CRAFTING_CONTROLLER = new BlockCraftingController(
            CRAFTING_CONTROLLER_RL);
    public static final BlockCraftingProcessor  CRAFTING_PROCESSOR  = new BlockCraftingProcessor(CRAFTING_PROCESSOR_RL);
    public static final BlockCraftingStorage    CRAFTING_STORAGE    = new BlockCraftingStorage(CRAFTING_STORAGE_RL);

    private static final Block[] BLOCKS = {//NOSONAR
                                           STORAGE_INTERFACE,
                                           STORAGE_INDEXER,
                                           TOKEN_GENERATOR,
                                           QUEUE,
                                           PATTERN_STORAGE,
                                           CRAFTING_CONTROLLER,
                                           CRAFTING_PROCESSOR,
                                           CRAFTING_STORAGE
    };

    private Blocks() {
    }

    /**
     * Register all Blocks when the Event occurs
     * and at the end as recommended by LexManos <a href="https://github.com/MinecraftForge/MinecraftForge/pull/4046#issuecomment-310527404" >here</a>.
     *
     * @param event the Register event
     */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        CoreLog.info("Registering Blocks");

        IForgeRegistry<Block> registry = event.getRegistry();
        registry.registerAll(BLOCKS);

        Tiles.registerTileEntities();
    }
}
