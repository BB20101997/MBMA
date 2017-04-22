package de.webtwob.mbma.common.crafting;

import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.interfaces.ICraftingNameInProgress;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by BB20101997 on 22. Apr. 2017.
 */
public class CraftingNode {
    
    protected CraftingNode parent;
    protected CraftingTree tree;
    protected ICraftingRequest request;
    protected boolean doneStatus = true;
    protected ICraftingNameInProgress iCraftingNameInProgress;
    protected Deque<Integer> stackIds = new LinkedList<>();
    
    public CraftingNode() {
        if(this instanceof CraftingTree){
            tree = (CraftingTree) this;
        }
    }
    
    /**
     * All requests past to this constructor will be assumed to be modifiable.
     * If you do NOT want your request to be changed copy it first!
     * */
    public CraftingNode(@Nullable CraftingNode parent, CraftingTree tree, ICraftingRequest request, ICraftingNameInProgress icnip) {
        this.parent = parent;
        this.tree = tree;
        this.request = request;
        iCraftingNameInProgress = icnip;
    }
    
    public boolean isDone(){
        return doneStatus;
    }
    
    /**
     * used by childes to pass the items to the parent node
     * since items should be stored in the ICraftingNameInProgress
     * only the id needs to be given to the parent
     * */
    protected void pushItemStackDown(int stackID){
        stackIds.push(stackID);
        //TODO reduce missing item count
    }
    
    /**
     * Does one cycle of the crafting operation
     * this is supposed to be called every tick and it
     * should not do all at once.
     *
     * @return  the next node to be called cycle up on
     *          null might be returned by the RootNode or in case of error
     */
    @Nullable
    protected CraftingNode cycle(){
        ItemStack stack = iCraftingNameInProgress.gatherMatchingStacks((stck)->false,request.getQuantity());
        iCraftingNameInProgress.putItemInStorage(stack);
        request.reduceQuantity(stack.getCount());
        if(request.isCompleted()){
            return parent;
        }else{
            //try crafting items with child nodes
            if(request.isCompleted()){
                return parent;
            }else{
                return this;
            }
        }
    }
}
