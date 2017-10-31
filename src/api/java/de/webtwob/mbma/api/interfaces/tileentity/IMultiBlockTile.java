package de.webtwob.mbma.api.interfaces.tileentity;

import de.webtwob.mbma.api.multiblock.MultiBlockGroup;
import de.webtwob.mbma.api.multiblock.MultiblockMember;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public interface IMultiBlockTile {

    MultiBlockGroup getGroup();

    void setGroup(MultiBlockGroup mbg);
    
    default void onBlockBreak(World world, BlockPos pos){
        MultiBlockGroup group = getGroup();
        if(group!=null){
            group.voidGroup();
            group.removeMember(new MultiblockMember(pos,world));
        }
    }

}
