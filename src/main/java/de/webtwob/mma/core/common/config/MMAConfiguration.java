package de.webtwob.mma.core.common.config;

import com.typesafe.config.impl.ConfigImpl;

import de.webtwob.mma.core.MMACore;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by BB20101997 on 31. Okt. 2017.
 */
@Config(modid = MMACore.MODID)
@Config.LangKey("mmacore:config.title")
public class MMAConfiguration extends ConfigImpl {
    
    @Config.Comment("Just a Test property")
    public static String stuff = "default";//NOSONAR
    
    @Config.Comment("How many requests can one Queue Block Store?")
    public static int queueLength = 5;//NOSONAR
    
    @Config.RangeInt(max = 64, min = 1)
    @Config.Comment("How many Stacks can one Crafting Storage Block store?")
    public static int storageStackLimit = 64;//NOSONAR
    
    @Config.RangeInt(min = 1, max = 64)
    @Config.Comment("How many Queues can be linked to one Controller?")
    public static int controllerQueueCount = 16;//NOSONAR
    
    @EventBusSubscriber(modid = MMACore.MODID)
    private static class EventHandler { //NOSONAR
        
        private EventHandler() {}
        
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MMACore.MODID)) {
                ConfigManager.sync(MMACore.MODID, Config.Type.INSTANCE);
            }
        }
        
    }
    
}
