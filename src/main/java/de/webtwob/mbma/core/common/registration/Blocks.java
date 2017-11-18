package de.webtwob.mbma.core.common.registration;

import de.webtwob.mbma.core.common.CoreLog;
import de.webtwob.mbma.core.common.block.*;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static de.webtwob.mbma.core.common.references.ResourceLocations.Blocks.*;


/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmacore")
public class Blocks {


    public static final BlockStorageInterface STORAGE_INTERFACE = new BlockStorageInterface();
    public static final BlockStorageIndexer STORAGE_INDEXER = new BlockStorageIndexer();

    public static final BlockRequestGenerator TOKEN_GENERATOR = new BlockRequestGenerator();

    public static final BlockQueue QUEUE = new BlockQueue();

    public static final BlockPatternStorage PATTERN_STORAGE = new BlockPatternStorage();

    public static final BlockCraftingController CRAFTING_CONTROLLER = new BlockCraftingController();
    public static final BlockCraftingProcessor CRAFTING_PROCESSOR = new BlockCraftingProcessor();
    public static final BlockCraftingStorage CRAFTING_STORAGE = new BlockCraftingStorage();

    private Blocks() {
    }

    /**
     * Register all Blocks when the Event occurs
     * and at the end as recommended by LexManos <a href="https://github.com/MinecraftForge/MinecraftForge/pull/4046#issuecomment-310527404" >here</a>.
     * @param event the Register event
     * */
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        CoreLog.info("Registering Blocks");

        IForgeRegistry<Block> registry = event.getRegistry();

        setNameAndRegister(STORAGE_INTERFACE, STORAGE_INTERFACE_RL, registry);
        setNameAndRegister(STORAGE_INDEXER, STORAGE_INDEXER_RL, registry);

        setNameAndRegister(TOKEN_GENERATOR, TOKEN_GENERATOR_REGISTRY_NAME, registry);

        setNameAndRegister(QUEUE, QUEUE_RL, registry);

        setNameAndRegister(PATTERN_STORAGE,PATTERN_STORAGE_REGISTRY_NAME,registry);

        setNameAndRegister(CRAFTING_CONTROLLER, CRAFTING_CONTROLLER_RL, registry);
        setNameAndRegister(CRAFTING_PROCESSOR, CRAFTING_PROCESSOR_RL, registry);
        setNameAndRegister(CRAFTING_STORAGE, CRAFTING_STORAGE_RL, registry);
        Tiles.registerTileEntities();
    }

    private static void setNameAndRegister(Block block, ResourceLocation rs, IForgeRegistry<Block> reg) {
        block.setRegistryName(rs);
        block.setUnlocalizedName(rs.toString());
        reg.register(block);
    }
}
