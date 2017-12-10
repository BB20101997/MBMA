package de.webtwob.mma.core.common.multiblockgroups;

import de.webtwob.mma.api.multiblock.InstantiatableGroupType;
import de.webtwob.mma.api.multiblock.MultiBlockGroup;
import de.webtwob.mma.api.multiblock.MultiBlockGroupTypeInstance;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
public class QueueGroupType extends InstantiatableGroupType {
    
    @Override
    public MultiBlockGroupTypeInstance createGroupTypeInstance(MultiBlockGroup group, Supplier markDirtyCallback) {
        return new QueueGroupType.Instance(group);
    }
    
    public class Instance implements MultiBlockGroupTypeInstance {
        
        private MultiBlockGroup group;
        
        public Instance(MultiBlockGroup group){
            this.group = group;
        }
        
        @Override
        public void joinData(@Nullable MultiBlockGroupTypeInstance oldInstance) {
        
        }
    
        @Override
        public void lostOnJoin() {
            //NO-OP
        }
        
        public List<Queue<ItemStack>> getQueues(){
            //TODO
            return null;
        }
        
    }
}
