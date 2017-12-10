package de.webtwob.mma.core.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
public  class MMAItemBlock extends ItemBlock {
    public MMAItemBlock(Block block) {
        super(block);
        setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        setUnlocalizedName(block.getUnlocalizedName());
    }
}
