package de.webtwob.mma.core.common.registration;

import de.webtwob.mma.api.registries.MultiBlockGroupType;
import de.webtwob.mma.core.common.CoreLog;
import de.webtwob.mma.core.common.multiblockgroups.QueueGroupType;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static de.webtwob.mma.core.common.references.ResourceLocations.MultiBlockGroups.*;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@Mod.EventBusSubscriber(modid = "mmacore")
public class MultiBlockGroupTypes {

    public static final MultiBlockGroupType  STORAGE = new MultiBlockGroupType();
    public static final MultiBlockGroupType  CRAFTING = new MultiBlockGroupType();
    public static final MultiBlockGroupType  RECIPES = new MultiBlockGroupType();
    public static final MultiBlockGroupType  QUEUE = new QueueGroupType();

    private MultiBlockGroupTypes() {
    }

    @SubscribeEvent
    public static void registerMBGT(RegistryEvent.Register<MultiBlockGroupType> event) {
        CoreLog.info("Registering MultiBlockGroupTypes");

        STORAGE.setRegistryName(MBGM_STORAGE);
        CRAFTING.setRegistryName(MBGM_CRAFTING);
        RECIPES.setRegistryName(MBGM_RECIPE);
        QUEUE.setRegistryName(MBGM_QUEUE);

        event.getRegistry().registerAll(STORAGE,CRAFTING,RECIPES,QUEUE);
    }

}
