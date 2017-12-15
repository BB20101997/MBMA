package de.webtwob.mma.api.util;

import net.minecraft.util.ResourceLocation;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
public class ResourceLocationUtils {

    private ResourceLocationUtils() {
    }

    public static String unlocalizedNameForResourceLocation(ResourceLocation rl) {
        return rl.toString().replace(":", ".");
    }

}
