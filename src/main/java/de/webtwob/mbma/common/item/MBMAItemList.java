package de.webtwob.mbma.common.item;

import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.block.MBMABlockList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
@Mod.EventBusSubscriber
public class MBMAItemList {

    public static final ItemBlock PSIItem = new ItemBlock(MBMABlockList.PSIBlock);

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        MBMALog.info("Registering Items");
        event.getRegistry().registerAll(new ItemBlock(MBMABlockList.PSIBlock).setRegistryName(MBMABlockList.PSIRegistryName));
    }



}
