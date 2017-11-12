package de.webtwob.mbma.api.multiblock;

import de.webtwob.mbma.api.APILog;
import de.webtwob.mbma.api.registries.MultiBlockGroupType;

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
    
    public MultiBlockGroupManager(WorldSavedData mbgmWorldSaveData) {
        data = mbgmWorldSaveData;
    }
    
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
     */
    public MultiBlockGroup createNewGroup(MultiBlockGroupType type) {
        MultiBlockGroup group = new MultiBlockGroup(this, type);
        groupMap.add(group);
        data.markDirty();
        return group;
    }
    
    public MultiBlockGroup getGroupForMember(final MultiBlockMember member, final MultiBlockGroupType type) {
        return groupMap.stream()
                .filter(MultiBlockGroup::isValid)
                .filter(g -> g.getType() == type)
                .filter(e -> e.isMemberOfGroup(member))
                .findAny().orElse(null);
    }
    
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
