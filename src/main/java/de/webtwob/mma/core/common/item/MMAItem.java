package de.webtwob.mma.core.common.item;

import de.webtwob.mma.api.util.ResourceLocationUtils;
import de.webtwob.mma.core.common.creativetab.CoreCreativeTab;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
public abstract class MMAItem extends Item{
    
    public MMAItem(ResourceLocation rl){
        setCreativeTab(CoreCreativeTab.MMATab);
        setRegistryName(rl);
        setUnlocalizedName(ResourceLocationUtils.unlocalizedNameForResourceLocation(rl));
    }
    
}
