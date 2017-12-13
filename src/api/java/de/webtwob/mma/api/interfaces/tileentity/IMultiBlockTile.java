package de.webtwob.mma.api.interfaces.tileentity;

import de.webtwob.mma.api.multiblock.MBGMWorldSaveData;
import de.webtwob.mma.api.multiblock.MultiBlockGroup;
import de.webtwob.mma.api.multiblock.MultiBlockMember;
import de.webtwob.mma.api.registries.MultiBlockGroupType;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IMultiBlockTile {

    /**
     * @param world the world the TileEntity is in
     * @param pos   the position of the TileEntity
     * @param type  what type of group to search for
     * @return the group the TileEntity is in or null if not in a Group of the type
     */
    @Nullable
    static MultiBlockGroup getGroup(World world, BlockPos pos, MultiBlockGroupType type) {
        MBGMWorldSaveData data = null;
        if(world!=null) {
            data= MBGMWorldSaveData.get(world);
        }
        if (data != null) {
            return data.multiBlockGroupManager.getGroupForMember(new MultiBlockMember(world, pos), type);
        }
        return null;
    }

    MultiBlockGroupType getGroupType();

    /**
     * Removes the Block from it's current group of the same type and adds it to the provided one
     *
     * @param world the world for the TileEntity
     * @param pos   the position of the TileEntity
     * @param mbg   the group to put this TileEntity in
     */
    default void setGroup(MultiBlockGroup mbg, World world, BlockPos pos) {
        MultiBlockGroup group = getGroup(world, pos, getGroupType());
        if (group != null && group != mbg) {
            group.removeMember(new MultiBlockMember(world, pos));

        }
    }

    /**
     * @param world the world the Block was broken in
     * @param pos the position the Block was broken at
     */
    default void onBlockBreak(World world, BlockPos pos) {
        MultiBlockGroup group = getGroup(world, pos, getGroupType());
        if (group != null) {
            group.voidGroup();
            group.removeMember(new MultiBlockMember(world, pos));
        }
    }

}
