package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.interfaces.tileentity.IDebugableTile;
import de.webtwob.mbma.api.interfaces.tileentity.IMultiBlockTile;
import de.webtwob.mbma.api.multiblock.MultiBlockGroup;
import de.webtwob.mbma.api.multiblock.MultiBlockGroupManager;
import de.webtwob.mbma.api.multiblock.MultiBlockMember;
import de.webtwob.mbma.core.common.MBMALog;

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
public abstract class MultiBlockTileEntity extends TileEntity implements IMultiBlockTile, IDebugableTile, ITickable {
    
    MultiBlockGroup group;
    
    public void update() {
        if (!world.isRemote) {
            MultiBlockGroup oldGroup = group;
            group = Arrays.stream(new BlockPos[]{pos, pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()})
                    .map(pos -> IMultiBlockTile.getGroup(world, pos, getGroupType()))
                    .filter(Objects::nonNull)
                    .reduce(MultiBlockGroup::joinGroups).orElse(null);
            if (group == null) {
                MBMALog.debug("Reduction concluded in null creating new Group!");
                MultiBlockGroupManager manager = MultiBlockGroupManager.getInstance(world);
                if (manager != null) {
                    group = manager.createNewGroup(getGroupType());
                }
            }
            if (oldGroup != group) {
                group.addMember(new MultiBlockMember(world, pos));
            }
        }
    }
    
    @Override
    public void performDebugOnTile(EntityPlayer player) {
        StringBuilder sb = new StringBuilder();
        sb.append("GroupType: ").append(group.getType().getRegistryName()).append('\n');
        sb.append("GroupHash: ").append(group.hashCode()).append('\n');
        player.sendStatusMessage(new TextComponentString(sb.toString()), false);
    }
}
