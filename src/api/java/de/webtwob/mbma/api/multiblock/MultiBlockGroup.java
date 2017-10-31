package de.webtwob.mbma.api.multiblock;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class MultiBlockGroup {
    
    private final Set<MultiblockMember> multiblockMemberSet = new HashSet<>();
    private final MultiBlockGroupManager manager;
    private final int id;
    private boolean isValid = true;
    
    public MultiBlockGroup(MultiBlockGroupManager manager, int id) {
        this.id = id;
        this.manager = manager;
    }
    
    /**
     * @param member the MultiblockMember to add
     * @return if adding did work
     * @throws IllegalStateException when called on an invalid group
     */
    public boolean addMember(MultiblockMember member) {
        if (!isValid) {
            throw new IllegalStateException("Adding Member to group not allowed for invalid Groups!");
        }
        return multiblockMemberSet.add(member);
    }
    
    /**
     * Call this when leaving a group
     * This way the id's of empty and invalid groups can be reused
     */
    public boolean removeMember(MultiblockMember member) {
        return multiblockMemberSet.remove(member);
    }
    
    /**
     * Will join two groups one will afterwards contain all Elements of both groups,
     * the other will be invalidated
     *
     * @param mbg the Group to join with this group
     * @return the joind group
     * @throws IllegalArgumentException if canGroupsBeJoined returns false
     */
    MultiBlockGroup joinGroups(MultiBlockGroup mbg) {
        if (!canGroupsBeJoined(mbg)) {
            throw new IllegalArgumentException("Tried to join Multiblockgroups from different managers!");
        }
        if (this.id < mbg.id) {
            multiblockMemberSet.addAll(mbg.multiblockMemberSet);
            mbg.voidGroup();
            return this;
        } else {
            mbg.multiblockMemberSet.addAll(multiblockMemberSet);
            voidGroup();
            return mbg;
        }
    }
    
    public boolean canGroupsBeJoined(@Nonnull MultiBlockGroup group) {
        return manager == group.manager && group.isValid() && group.isValid();
    }
    
    /**
     * Invalidates the group,
     * used inside the join for one of the groups,
     * also used when a block is removed to redetermine which Blocks are and aren't part of the same group
     */
    public void voidGroup() {
        isValid = false;
    }
    
    /**
     * true till voidGroup() has been called
     */
    public boolean isValid() {
        return isValid;
    }
    
    /**
     * @return the Members of the Group
     */
    public List<MultiblockMember> getMembers() {
        return new ArrayList<>(multiblockMemberSet);
    }
    
    public boolean isMemberOfGroup(MultiblockMember member) {
        return multiblockMemberSet.contains(member);
    }
    
    public int getID() {
        return id;
    }
    
    public MultiBlockGroupManager getManager() {
        return manager;
    }
}
