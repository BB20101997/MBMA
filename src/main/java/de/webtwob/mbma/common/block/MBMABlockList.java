package de.webtwob.mbma.common.block;

import de.webtwob.mbma.common.MBMALog;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@Mod.EventBusSubscriber
public class MBMABlockList {

    public static final PermanentStorageInterfaceBlock PSIBlock = new PermanentStorageInterfaceBlock();
    public static final String PSIRegistryName = "psib";

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        MBMALog.info("Registering Blocks");
        PSIBlock.setRegistryName(PSIRegistryName);
        event.getRegistry().registerAll(MBMABlockList.PSIBlock);
    }
}
