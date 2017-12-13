package de.webtwob.mma.core.common.multiblockgroups;

import de.webtwob.mma.api.crafting.ItemStackContainer;
import de.webtwob.mma.api.multiblock.InstantiatableGroupType;
import de.webtwob.mma.api.multiblock.MultiBlockGroup;
import de.webtwob.mma.api.multiblock.MultiBlockGroupTypeInstance;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
public class QueueGroupType extends InstantiatableGroupType {
    
    @Override
    public MultiBlockGroupTypeInstance createGroupTypeInstance(MultiBlockGroup group, Supplier markDirtyCallback) {
        return new QueueGroupType.Instance();
    }
    
    public class Instance implements MultiBlockGroupTypeInstance {
        
        private final LinkedList<ItemStackContainer> containers = new LinkedList<>();
        
        @Override
        public void joinData(@Nullable MultiBlockGroupTypeInstance oldInstance) {
            if(oldInstance instanceof QueueGroupType.Instance){
                containers.addAll(((Instance) oldInstance).getQueue());
            }
        }
    
        @Override
        public void lostOnJoin() {
            //NO-OP
        }
        
        public LinkedList<ItemStackContainer> getQueue(){
            return containers;
        }
    
        @Override
        public String toString() {
            return String.format("QueueGroupType: List size: %d",containers.size());
        }
    }
}
