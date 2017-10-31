package de.webtwob.mbma.api;

import net.minecraft.util.ResourceLocation;

import static de.webtwob.mbma.api.MBMA_API.MODID;

/**
 * Created by BB20101997 on 26. Okt. 2017.
 */
public class MBMA_API_Constants {
    
    public static final String LINK_SHARE_POS = MODID + ":LinkSharePos";
    public static final String TOKEN_SHARE_QUANTITY = MODID + ":TokenQuantity";
    public static final String TOKEN_SHARE_REQUEST = MODID + ":TokenRequest";
    
    //Registrie ResourceLocations
    public static final ResourceLocation REG_MULTIBLOCK = new ResourceLocation(MODID,"multiblock");
    
    private MBMA_API_Constants() {
    }
}
