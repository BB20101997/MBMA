package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.interfaces.tileentity.IDebuggableTile;
import de.webtwob.mma.api.interfaces.tileentity.IMultiBlockTile;
import de.webtwob.mma.api.multiblock.MultiBlockGroup;
import de.webtwob.mma.api.multiblock.MultiBlockGroupManager;
import de.webtwob.mma.api.multiblock.MultiBlockGroupTypeInstance;
import de.webtwob.mma.api.multiblock.MultiBlockMember;
import de.webtwob.mma.core.common.CoreLog;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public abstract class MultiBlockTileEntity extends TileEntity implements IMultiBlockTile, IDebuggableTile, ITickable {

    MultiBlockGroup group;

    public void update() {
        if (!world.isRemote) {
            MultiBlockGroup oldGroup = group;
            group = Arrays.stream(
                    new BlockPos[]{pos, pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()})
                          .map(pos -> IMultiBlockTile.getGroup(world, pos, getGroupType()))
                          .filter(Objects::nonNull)
                          .reduce(MultiBlockGroup::joinGroups)
                          .orElse(null);
            if (null == group) {
                CoreLog.debug("Reduction concluded in null creating new Group!");
                MultiBlockGroupManager manager = MultiBlockGroupManager.getInstance(world);
                if (null != manager) {
                    group = manager.createNewGroup(getGroupType());
                }
            }
            if (null != group && oldGroup != group) {
                group.addMember(new MultiBlockMember(world, pos));
            }
        }
    }

    @Override
    public void performDebugOnTile(EntityPlayer player) {
        StringBuilder sb = new StringBuilder();
        if (null != group) {
            sb.append("GroupType: ").append(group.getType().getRegistryName()).append('\n');
            MultiBlockGroupTypeInstance instance = group.getTypeInstance();
            if (null != instance) {
                sb.append("GroupInstance: ").append(instance).append('\n');
            } else {
                sb.append("GroupInstance: null \n");
            }
            sb.append("GroupHash: ").append(group.hashCode()).append('\n');
            sb.append("GroupSize: ").append(group.getMembers().size()).append('\n');
        } else {
            sb.append("Group is null;\n");
        }
        player.sendStatusMessage(new TextComponentString(sb.toString()), false);
    }
}
