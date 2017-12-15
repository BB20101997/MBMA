package de.webtwob.mma.api.multiblock;

import de.webtwob.mma.api.APILog;
import de.webtwob.mma.api.registries.MultiBlockGroupType;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */

public class MultiBlockGroupManager implements INBTSerializable<NBTTagList> {

    WorldSavedData data;
    private Set<MultiBlockGroup> groupMap = new HashSet<>();

    /**
     * @param mbgmWorldSaveData the WorldSaveData Instance this MultiBlockGroupManager is saved in
     */
    public MultiBlockGroupManager(WorldSavedData mbgmWorldSaveData) {
        data = mbgmWorldSaveData;
    }

    /**
     * @param world the world used to get access to the world Storage of the save
     *
     * @return the saves MultiBlockGroupManager Instance
     */
    @Nullable
    public static MultiBlockGroupManager getInstance(World world) {
        MBGMWorldSaveData data = MBGMWorldSaveData.get(world);
        if (data != null) {
            return data.multiBlockGroupManager;
        }
        return null;
    }

    /**
     * I do not expect an integer overflow to happen in any some what likely scenario
     *
     * @param type the type for which a new Group shall be created
     *
     * @return the created MultiBlockGroup instance
     */
    public MultiBlockGroup createNewGroup(MultiBlockGroupType type) {
        MultiBlockGroup group = new MultiBlockGroup(this, type);
        groupMap.add(group);
        data.markDirty();
        return group;
    }

    /**
     * @param member the MultiBlockGroupMember for which to find the associated group
     * @param type   the type of group to search for
     *
     * @return any group containing member that is of the type type or null if none is found
     */
    public MultiBlockGroup getGroupForMember(final MultiBlockMember member, final MultiBlockGroupType type) {
        return groupMap.stream()
                       .filter(MultiBlockGroup::isValid)
                       .filter(g -> g.getType() == type)
                       .filter(e -> e.isMemberOfGroup(member))
                       .findAny()
                       .orElse(null);
    }

    /**
     * @param group the group that shall no longer be managed by this MultiBlockGroupManager
     */
    public void removeGroup(MultiBlockGroup group) {
        group.voidGroup();
        groupMap.remove(group);
        data.markDirty();
    }

    @Override
    public NBTTagList serializeNBT() {
        NBTTagList groupList = new NBTTagList();
        for (MultiBlockGroup entry : groupMap) {
            if (entry != null && entry.isValid() && !entry.getMembers().isEmpty()) {
                groupList.appendTag(entry.serializeNBT());
            }
        }
        APILog.info("Serialized " + groupList.tagCount() + " group(s).");
        return groupList;
    }

    @Override
    public void deserializeNBT(final NBTTagList nbt) {
        groupMap.forEach(MultiBlockGroup::voidGroup);
        groupMap.clear();
        MultiBlockGroup group;
        for (NBTBase base : nbt) {
            if (base instanceof NBTTagCompound) {
                group = new MultiBlockGroup(this);
                group.deserializeNBT((NBTTagCompound) base);
                groupMap.add(group);
            }
        }
        APILog.info("Deserialized " + groupMap.size() + " group(s).");
    }
}
