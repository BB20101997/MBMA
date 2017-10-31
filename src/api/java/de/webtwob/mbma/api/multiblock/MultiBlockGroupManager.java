package de.webtwob.mbma.api.multiblock;

import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */

public class MultiBlockGroupManager extends IForgeRegistryEntry.Impl<MultiBlockGroupManager>{
   
    //TODO save groups
    
    private Map<Integer, MultiBlockGroup> groupMap = new HashMap<>();
    private int largestKey = 0;
    
    public static void register() {
    
    }
    
    @Nullable
    public MultiBlockGroup getGroupForID(int id) {
        MultiBlockGroup group = groupMap.getOrDefault(id, null);
        if (group != null && group.isValid()) {
            return group;
        } else {
            groupMap.remove(id);
            return null;
        }
    }
    
    /**
     * I do not expect an integer overflow to happen in any some what likely scenario
     */
    public MultiBlockGroup createNewGroup() {
        MultiBlockGroup group = new MultiBlockGroup(this,largestKey++);
        groupMap.put(group.getID(),group );
        return group;
    }
    
    public MultiBlockGroup getGroupForMember(final MultiblockMember member) {
        return groupMap.values().stream().filter(e -> e.isMemberOfGroup(member)).findAny().orElse(null);
    }
    
}
