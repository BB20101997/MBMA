package de.webtwob.mbma.common.capability;

import de.webtwob.mbma.common.item.MBMAItemList;
import de.webtwob.mbma.common.tileentity.QSTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 17. Mär. 2017.
 */
public class QSItemHandler implements IItemHandler, IItemHandlerModifiable {

    private final        NonNullList<ItemStack> stacks     = NonNullList.withSize(getSlots(), ITEM_STACK);
    private final        ItemStack[]            itemStacks = new ItemStack[getSlots()];
    private static final ItemStack              ITEM_STACK = new ItemStack(MBMAItemList.LINKCARD, 0);

    private final QSTileEntity tileEntity;

    public QSItemHandler(QSTileEntity te) {
        tileEntity = te;
    }

    @Override
    public int getSlots() {
        return 3 * 6;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return stacks.get(slot).copy();
    }

    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(stacks.get(slot).getCount() < 1 && stack.getItem() == MBMAItemList.LINKCARD) {
            if(!simulate) { stacks.get(slot).grow(1); tileEntity.markDirty();}
            ItemStack ret = stack.copy();
            ret.shrink(1);
            return ret;
        } else { return stack; }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      if(stacks.get(slot).getCount()>0){
          if(!simulate){
              stacks.get(slot).shrink(1);
              tileEntity.markDirty();
          }
          return new ItemStack(MBMAItemList.LINKCARD,1);
      } else{
          return ItemStack.EMPTY;
      }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if(!stack.isEmpty()){
            stacks.set(slot,stack);
        }else{
            stacks.set(slot,ITEM_STACK);
        }
        tileEntity.markDirty();
    }
}
