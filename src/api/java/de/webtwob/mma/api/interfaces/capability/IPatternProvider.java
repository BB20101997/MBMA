package de.webtwob.mma.api.interfaces.capability;

import de.webtwob.mma.api.capability.APICapabilities;

import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * Created by BB20101997 on 03. Dez. 2017.
 */
public interface IPatternProvider {
    
    static  IPatternProvider getIPatternProviderForTileEntity(TileEntity te){
        if(te==null){
            return null;
        }
        return te.getCapability(APICapabilities.CAPABILITY_PATTERN_PROVIDER,null);
    }
    
    public List<ICraftingRecipe> getPatternList();
    
}
