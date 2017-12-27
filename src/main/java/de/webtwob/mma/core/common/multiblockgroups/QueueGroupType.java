package de.webtwob.mma.core.common.multiblockgroups;

import de.webtwob.mma.api.crafting.ItemStackContainer;
import de.webtwob.mma.api.interfaces.tileentity.IMultiBlockTile;
import de.webtwob.mma.api.multiblock.InstantiatableGroupType;
import de.webtwob.mma.api.multiblock.MultiBlockGroup;
import de.webtwob.mma.api.multiblock.MultiBlockGroupTypeInstance;
import de.webtwob.mma.core.common.tileentity.TileEntityQueue;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
public class QueueGroupType extends InstantiatableGroupType {


    @Nullable
    public static QueueGroupType.Instance getInstance(TileEntityQueue teq){
        MultiBlockGroup group = IMultiBlockTile.getGroup(teq.getWorld(), teq.getPos(), teq.getGroupType());
        if (null != group) {
            MultiBlockGroupTypeInstance instance = group.getTypeInstance();
            if (instance instanceof QueueGroupType.Instance) {
                return (Instance) instance;
            }
        }
        return null;
    }

    @Override
    public MultiBlockGroupTypeInstance createGroupTypeInstance(MultiBlockGroup group, Runnable markDirtyCallback) {
        return new QueueGroupType.Instance(group);
    }

    public class Instance implements MultiBlockGroupTypeInstance {

        MultiBlockGroup group;

        private final LinkedList<ItemStackContainer> containers = new LinkedList<>();

        Instance(final MultiBlockGroup group) {
            this.group = group;
        }

        @Override
        public void joinData(@Nullable MultiBlockGroupTypeInstance oldInstance) {
            if (oldInstance instanceof QueueGroupType.Instance) {
                containers.addAll(((Instance) oldInstance).getQueue());
            }
        }

        @Override
        public void lostOnJoin() {
            //NO-OP
        }

        public Queue<ItemStackContainer> getQueue() {
            return containers;
        }

        @Override
        public String toString() {
            return String.format("QueueGroupType: List size: %d", containers.size());
        }


    }
}
