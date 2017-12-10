package de.webtwob.mma.core.common.block;

import de.webtwob.mma.core.common.creativetab.CoreCreativeTab;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
public abstract class MMABlock extends net.minecraft.block.Block{
    
    public MMABlock(Material blockMaterialIn, MapColor blockMapColorIn,ResourceLocation rl) {
        super(blockMaterialIn, blockMapColorIn);
        setCreativeTab(CoreCreativeTab.MMATab);
        setRegistryName(rl);
        setUnlocalizedName(rl.toString());
    }
    
    public MMABlock(Material materialIn, ResourceLocation rl) {
        this(materialIn,materialIn.getMaterialMapColor(),rl);
    }
}
