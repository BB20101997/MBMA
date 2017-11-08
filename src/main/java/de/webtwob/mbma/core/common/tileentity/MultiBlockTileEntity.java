package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.interfaces.tileentity.IDebugableTile;
import de.webtwob.mbma.api.interfaces.tileentity.IMultiBlockTile;
import de.webtwob.mbma.api.multiblock.MultiBlockGroup;
import de.webtwob.mbma.api.multiblock.MultiBlockGroupManager;
import de.webtwob.mbma.api.multiblock.MultiblockMember;
import de.webtwob.mbma.api.util.multiblock.MultiBlockUtils;
import de.webtwob.mbma.core.common.references.MBMA_NBTKeys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public abstract class MultiBlockTileEntity extends TileEntity implements IMultiBlockTile, IDebugableTile, ITickable {
    
    protected volatile MultiBlockGroup group = null;
    
    public abstract MultiBlockGroupManager getManager();
    
    @Override
    public synchronized MultiBlockGroup getGroup() {
        return group;
    }
    
    @Override
    public void setGroup(MultiBlockGroup mbg) {
        group = mbg;
    }
    
    public void update() {
        if (!world.isRemote) {
            int smallestID = Integer.MAX_VALUE;
            MultiBlockGroup groupSmallestID = null;
            if (group != null && group.isValid()) {
                smallestID = group.getID();
                groupSmallestID = group;
            }
            BlockPos[] neighbours = new BlockPos[]{pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()};
            for (BlockPos pos : neighbours) {
                MultiBlockGroup group = MultiBlockUtils.getGroupForPosition(world, pos);
                if (group != null && group.isValid() && group.getID() < smallestID && group.getManager() == getManager()) {
                    smallestID = group.getID();
                    groupSmallestID = group;
                }
            }
            if (groupSmallestID == null) {
                group = getManager().createNewGroup();
                group.addMember(new MultiblockMember(pos, world));
            } else {
                if (group != groupSmallestID) {
                    MultiblockMember member = new MultiblockMember(pos, world.provider.getDimension());
                    if(group!=null) {
                        group.voidGroup();
                        group.removeMember(member);
                    }
                    group = groupSmallestID;
                    group.addMember(member);
                    markDirty();
                }
            }
        }
    }
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound compound1 = super.writeToNBT(compound);
        //noinspection ConstantConditions
        if (compound1 == null) {
            compound1 = new NBTTagCompound();
        }
        if(group!=null) {
            compound1.setInteger(MBMA_NBTKeys.MULTIBLOCK_GROUP_ID, group.getID());
        }
        return compound1;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey(MBMA_NBTKeys.MULTIBLOCK_GROUP_ID, Constants.NBT.TAG_INT)) {
            group = getManager().getGroupForID(compound.getInteger(MBMA_NBTKeys.MULTIBLOCK_GROUP_ID));
        }
    }
    
    @Override
    public void performDebugOnTile(EntityPlayer player) {
        player.sendStatusMessage(new TextComponentString("GroupID: " + ((group != null) ? group.getID() : "null")), false);
    }
}
