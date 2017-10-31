package de.webtwob.mbma.api.registries;

import de.webtwob.mbma.api.MBMA_API_Constants;
import de.webtwob.mbma.api.multiblock.MultiBlockGroupManager;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@Mod.EventBusSubscriber(modid = "mbmaapi")
public class MBMARegistries {
    
    private MBMARegistries() {
    }
    
    @SubscribeEvent
    public static void registerRegistries(RegistryEvent.NewRegistry event){
        RegistryBuilder<MultiBlockGroupManager> builder = new RegistryBuilder<>();
        builder.setName(MBMA_API_Constants.REG_MULTIBLOCK);
        builder.setType(MultiBlockGroupManager.class);
        builder.allowModification();
        builder.create();
    }
    
    
}
