package de.webtwob.mbma.core.common.crafting.old;

import de.webtwob.mbma.api.interfaces.ICraftingAccessor;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;
import de.webtwob.mbma.core.common.tileentity.old.TileEntityQueueOld;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by BB20101997 on 07. Mai. 2017.
 */
@Deprecated
public class QSCraftingAccessor implements ICraftingAccessor {

    private final QSData data;
    private final TileEntityQueueOld tileEntityQueue;
    private final ICraftingRequest request;

    QSCraftingAccessor(QSData data, ICraftingRequest request) {
        this.data = data;
        this.tileEntityQueue = data.tileEntityQueue;
        this.request = request;
    }

    public ICraftingRequest getRequest() {
        return request;
    }

    @Override
    public int stackToTemp(@Nonnull ItemStack stack) {
        data.collectList.add(stack);
        return 0;
    }

    @Override
    public void dropTemp() {
        //TODO
    }

    @Override
    public ItemStack peekAtItemInTemp(int id) {
        return null;
    }

    /*
    @Override
    public List<RecipePage> getRecipePages() {
        return data.recipeList.stream()
                .map(IBlockPosProvider::getBlockPos)
                .filter(Objects::nonNull)
                .map(tileEntityQueue.getWorld()::getTileEntity)
                .filter(t -> t instanceof TileEntityRecipeStoreOld)
                .map(t -> (TileEntityRecipeStoreOld) t)
                .flatMap(r -> r.getPages().stream())
                .collect(Collectors.toList());
    }
*/
    @Override
    public int[] gatherMatchingStacks(Predicate<ItemStack> filter, int amount) {
        List<IItemHandler> inventories = data.permLinkList.stream()
                .map(item -> data.getInventoryFromLink(item, tileEntityQueue.getWorld()))
                .filter(Objects::nonNull).collect(Collectors.toList());
        List<Integer> ids = new ArrayList<>();
        ItemStack stack;
        out:
        for (IItemHandler inv : inventories) {
            for (int i = 0; i < inv.getSlots(); i++) {
                if (filter.test(inv.getStackInSlot(i))) {
                    stack = inv.extractItem(i, amount, false);
                    amount -= stack.getCount();
                    ids.add(stackToTemp(stack));
                    if (amount <= 0) {
                        break out;
                    }
                }
            }
        }
        return ids.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public void returnStackToPerm(int stackID) {

    }

    private ItemStack moveStack(ItemStack stack, final ItemStackHandler destination) {
        IItemHandler inv;
        for (int linkID = 0; linkID < destination.getSlots(); linkID++) {
            inv = data.getInventoryFromLink(destination.getStackInSlot(linkID), tileEntityQueue.getWorld());
            if (inv != null) {
                stack = ItemHandlerHelper.insertItemStacked(inv, stack, false);
            }
            if (stack.isEmpty()) {
                break;
            }
        }
        return stack;
    }
}
