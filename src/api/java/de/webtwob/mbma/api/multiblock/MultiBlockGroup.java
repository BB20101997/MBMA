package de.webtwob.mbma.api.multiblock;

import de.webtwob.mbma.api.MBMA_API_Constants;
import de.webtwob.mbma.api.registries.MultiBlockGroupType;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static de.webtwob.mbma.api.MBMA_API_Constants.NBT.GROUP_TYPE;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class MultiBlockGroup implements INBTSerializable<NBTTagCompound> {
    
    private final Set<MultiBlockMember> multiblockMemberSet = new HashSet<>();
    private final MultiBlockGroupManager manager;
    private MultiBlockGroupType type;
    private boolean isValid = true;
    
    public MultiBlockGroup(MultiBlockGroupManager mbgm) {
        manager = mbgm;
    }
    
    public MultiBlockGroup(MultiBlockGroupManager mbgm,MultiBlockGroupType type) {
        this(mbgm);
        this.type = type;
    }
    
    /**
     * @param member the MultiBlockMember to addd
     * @throws IllegalStateException when called on an invalid group
     */
    public void addMember(MultiBlockMember member) {
        if (!isValid) {
            throw new IllegalStateException("Adding Member to group not allowed for invalid Groups!");
        }
        multiblockMemberSet.add(member);
        markDirty();
    }
    
    /**
     * Call this when leaving a group
     * This way the id's of empty and invalid groups can be reused
     */
    public void removeMember(MultiBlockMember member) {
        if (multiblockMemberSet.contains(member)) {
            voidGroup();
            multiblockMemberSet.remove(member);
            markDirty();
        }
    }
    
    /**
     * All elements will be added to the larger group or this
     * the other group will be invalidated
     *
     * @param mbg the Group to join with this group
     * @return the joind group
     * @throws IllegalArgumentException if canGroupsBeJoined returns false
     */
    public MultiBlockGroup joinGroups(MultiBlockGroup mbg) {
        if (this == mbg||mbg==null) return this;
        if (!canGroupsBeJoined(mbg)) {
            throw new IllegalArgumentException("Tried to join MultiBlockGroups that don't qualify to be joined!");
        }
        if (multiblockMemberSet.size() < mbg.multiblockMemberSet.size()) {
            mbg.multiblockMemberSet.addAll(multiblockMemberSet);
            voidGroup();
            markDirty();
            return mbg;
        } else {
            multiblockMemberSet.addAll(mbg.multiblockMemberSet);
            mbg.voidGroup();
            markDirty();
            return this;
        }
    }
    
    public boolean canGroupsBeJoined(@Nonnull MultiBlockGroup group) {
        return group.type == type && isValid() && group.isValid();
    }
    
    /**
     * Invalidates the group,
     * used inside the join for one of the groups,
     * also used when a block is removed to redetermine which Blocks are and aren't part of the same group
     */
    public void voidGroup() {
        isValid = false;
        multiblockMemberSet.clear();
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
    public Set<MultiBlockMember> getMembers() {
        return Collections.unmodifiableSet(multiblockMemberSet);
    }
    
    public boolean isMemberOfGroup(MultiBlockMember member) {
        return multiblockMemberSet.contains(member);
    }
    
    public MultiBlockGroupType getType() {
        return type;
    }
    
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        if (type != null) {
            compound.setString(GROUP_TYPE, type.getRegistryName().toString());
        }
        NBTTagList memberList = new NBTTagList();
        for(MultiBlockMember member:multiblockMemberSet){
            memberList.appendTag(member.serializeNBT());
        }
        compound.setTag(MBMA_API_Constants.NBT.MBG_MEMBERS, memberList);
        return compound;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void deserializeNBT(final NBTTagCompound compound) {
        if (compound.hasKey(GROUP_TYPE, Constants.NBT.TAG_INT_ARRAY)) {
            type = GameRegistry.findRegistry(MultiBlockGroupType.class).getValue(new ResourceLocation(compound.getString(GROUP_TYPE)));
        }
        
        if (compound.hasKey(MBMA_API_Constants.NBT.MBG_MEMBERS, Constants.NBT.TAG_INT_ARRAY)) {
            NBTTagList memberList = compound.getTagList(MBMA_API_Constants.NBT.MBG_MEMBERS, Constants.NBT.TAG_INT_ARRAY);
            MultiBlockMember member;
            for (NBTBase base2 : memberList) {
                if (base2 instanceof NBTTagIntArray) {
                    member = new MultiBlockMember();
                    member.deserializeNBT((NBTTagIntArray) base2);
                    addMember(member);
                }
            }
        }
    }
    
    private void markDirty(){
        manager.data.markDirty();
    }
}
