package de.webtwob.mbma.common.block;

import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.references.MBMAResources;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;


/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber
public class MBMABlockList {

    public static final StorageInterfaceBlock STORAGE_INTERFACE_BLOCK = new StorageInterfaceBlock();
    public static final QueueStackBlock QUEUE_STACK_BLOCK = new QueueStackBlock();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        MBMALog.info("Registering Blocks");

        //set register names
        STORAGE_INTERFACE_BLOCK.setRegistryName(MBMAResources.INTERFACE_REGISTRY_NAME);
        QUEUE_STACK_BLOCK.setRegistryName(MBMAResources.QUEUESTACK_REGISTRY_NAME);

        //set Unlocalized names
        STORAGE_INTERFACE_BLOCK.setUnlocalizedName(MBMAResources.INTERFACE_REGISTRY_NAME.toString());
        QUEUE_STACK_BLOCK.setUnlocalizedName(MBMAResources.QUEUESTACK_REGISTRY_NAME.toString());

        IForgeRegistry<Block> registry = event.getRegistry();

        //register blocks
        registry.registerAll(STORAGE_INTERFACE_BLOCK, QUEUE_STACK_BLOCK);
    }
}
