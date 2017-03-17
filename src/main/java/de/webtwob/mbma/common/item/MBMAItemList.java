package de.webtwob.mbma.common.item;

import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.block.MBMABlockList;
import de.webtwob.mbma.common.block.QueueStackBlock;
import de.webtwob.mbma.common.references.RegistryNames;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
@Mod.EventBusSubscriber
public class MBMAItemList {

    public static final ItemBlock PSIItem = new ItemBlock(MBMABlockList.PSIBlock);
    public static final ItemBlock QueueStackItem = new ItemBlock(MBMABlockList.QUEUE_STACK_BLOCK);
    public static final Item LINKCARD = new LinkCardItem();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        MBMALog.info("Registering Items");

        //set Register names
        PSIItem.setRegistryName(RegistryNames.PSI_REGISTRY_NAME);
        LINKCARD.setRegistryName(RegistryNames.LINKCARD_REGISTRY_NAME);
        QueueStackItem.setRegistryName(RegistryNames.QUEUESTACK_REGISTRY_NAME);

        IForgeRegistry<Item> registry = event.getRegistry();

        //register items
        registry.registerAll(PSIItem,LINKCARD,QueueStackItem);
    }



}
