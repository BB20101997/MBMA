package de.webtwob.mbma.api.interfaces.tileentity;

import de.webtwob.mbma.api.multiblock.MBGMWorldSaveData;
import de.webtwob.mbma.api.multiblock.MultiBlockGroup;
import de.webtwob.mbma.api.multiblock.MultiBlockMember;
import de.webtwob.mbma.api.registries.MultiBlockGroupType;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IMultiBlockTile {

    MultiBlockGroupType getGroupType();
    
    static MultiBlockGroup getGroup(World world, BlockPos pos,MultiBlockGroupType type) {
        MBGMWorldSaveData data = MBGMWorldSaveData.get(world);
        if(data!=null){
            return data.multiBlockGroupManager.getGroupForMember(new MultiBlockMember(world, pos),type);
        }
        return null;
    }
    
    default void setGroup(MultiBlockGroup mbg,World world,BlockPos pos){
        MultiBlockGroup group = getGroup(world,pos,getGroupType());
        if(group!=null&&group!=mbg){
            group.removeMember(new MultiBlockMember(world,pos));
            
        }
    }
    
    default void onBlockBreak(World world, BlockPos pos){
        MultiBlockGroup group = getGroup(world,pos,getGroupType());
        if(group!=null){
            group.voidGroup();
            group.removeMember(new MultiBlockMember(world, pos));
        }
    }

}
