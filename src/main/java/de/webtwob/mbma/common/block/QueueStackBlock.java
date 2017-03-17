package de.webtwob.mbma.common.block;

import de.webtwob.mbma.common.creativetab.MBMACreativeTab;
import de.webtwob.mbma.common.references.UnlocalizedNames;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by bennet on 17.03.17.
 */
public class QueueStackBlock extends Block{

    public QueueStackBlock() {
        super(Material.IRON);
        setCreativeTab(MBMACreativeTab.MBMATab);
        setUnlocalizedName(UnlocalizedNames.QUEUESTACK_NAME);
    }

}
