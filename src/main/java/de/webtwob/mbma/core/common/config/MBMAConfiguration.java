package de.webtwob.mbma.core.common.config;

import com.typesafe.config.impl.ConfigImpl;
import de.webtwob.mbma.core.MBMACore;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by BB20101997 on 31. Okt. 2017.
 */
@Config(modid = MBMACore.MODID)
@Config.LangKey("mbmacore:config.title")
public class MBMAConfiguration extends ConfigImpl {
    
    @Config.Comment("Just a Test property")
    public static String stuff = "default";
    
    @Config.Comment("How many requests can one Queue Block Store?")
    public static int queueLength = 5;
    
    @Config.Comment("How many Stacks can one Crafting Storage Block store?")
    public static int storageStackLimit = 64;
    
    
    @EventBusSubscriber(modid = MBMACore.MODID)
    private static class EventHandler{
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event){
            if(event.getModID().equals(MBMACore.MODID)){
                ConfigManager.sync(MBMACore.MODID,Config.Type.INSTANCE);
            }
        }
    }
    
}
