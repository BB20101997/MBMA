package de.webtwob.mbma.core.common.registration;

import de.webtwob.mbma.api.registries.MultiBlockGroupType;
import de.webtwob.mbma.core.common.MBMALog;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static de.webtwob.mbma.core.common.references.MBMAResourceLocations.MultiBlockGroups.*;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmacore")
public class MBMAMultiBlockGroupManagers {
    
    public static final MultiBlockGroupType  STORAGE = new MultiBlockGroupType();
    public static final MultiBlockGroupType  CRAFTING = new MultiBlockGroupType();
    public static final MultiBlockGroupType  RECIPES = new MultiBlockGroupType();
    public static final MultiBlockGroupType  QUEUE = new MultiBlockGroupType();
    
    private MBMAMultiBlockGroupManagers() {
    }
    
    @SubscribeEvent
    public static void registerMBGM(RegistryEvent.Register<MultiBlockGroupType> event) {
        MBMALog.info("Registering MultiBlockGroupManagers");
        
        STORAGE.setRegistryName(MBGM_STORAGE);
        CRAFTING.setRegistryName(MBGM_CRAFTING);
        RECIPES.setRegistryName(MBGM_RECIPE);
        QUEUE.setRegistryName(MBGM_QUEUE);
        
        event.getRegistry().registerAll(STORAGE,CRAFTING,RECIPES,QUEUE);
    }
    
}
