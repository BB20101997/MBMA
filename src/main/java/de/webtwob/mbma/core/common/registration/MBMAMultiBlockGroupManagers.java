package de.webtwob.mbma.core.common.registration;

import de.webtwob.mbma.api.multiblock.MultiBlockGroupManager;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.MultiBlockGroups.*;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmacore")
public class MBMAMultiBlockGroupManagers {
    
    public static final MultiBlockGroupManager  STORAGE = new MultiBlockGroupManager();
    public static final MultiBlockGroupManager  CRAFTING = new MultiBlockGroupManager();
    public static final MultiBlockGroupManager  RECIPES = new MultiBlockGroupManager();
    public static final MultiBlockGroupManager  QUEUE = new MultiBlockGroupManager();
    
    private MBMAMultiBlockGroupManagers() {
    }
    
    @SubscribeEvent
    public static void registerMBGM(RegistryEvent.Register<MultiBlockGroupManager> event) {
        STORAGE.setRegistryName(MBGM_STORAGE);
        CRAFTING.setRegistryName(MBGM_CRAFTING);
        RECIPES.setRegistryName(MBGM_RECIPE);
        QUEUE.setRegistryName(MBGM_QUEUE);
        
        event.getRegistry().registerAll(STORAGE,CRAFTING,RECIPES,QUEUE);
    }
    
}
