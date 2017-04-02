package de.webtwob.mbma.common.inventory;

import de.webtwob.mbma.api.capability.implementations.ReplaceableItemHandler;
import de.webtwob.mbma.api.interfaces.IObjectCondition;
import de.webtwob.mbma.common.tileentity.RecipeBankTileEntity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by bennet on 01.04.17.
 */
public class RecipeBankContainer extends Container {
    
    EntityPlayer player;
    private RecipeBankTileEntity tileEntity;
    private static final int                             xSize        = 256;
    private static final int                             ySize        = 223;
    private static final int                             linkX        = 138;
    private static final int                             linkY        = 78;
    private static final int                             patternX     = 12;
    private static final int                             patternY     = 6;
    private static final NonNullList<ItemStack>          dummyLinks   = NonNullList.withSize(18, ItemStack.EMPTY);
    private static final NonNullList<ItemStack>          dummyRecipes = NonNullList.withSize(42, ItemStack.EMPTY);
    private              RecipeBankTileEntity.RecipePage currentPage  = null;
    
    private final IObjectCondition<ItemStack> LINK_FILTER   = stack -> currentPage != null && MBMAFilter.LINK_FILTER.checkCondition(stack);
    private final IObjectCondition<ItemStack> RECIPE_FILTER = stack -> currentPage != null && MBMAFilter.RECIPE_FILTER.checkCondition(stack);
    
    private ReplaceableItemHandler links, recipes;
    
    public RecipeBankContainer(EntityPlayer player, RecipeBankTileEntity te) {
        this.player = player;
        tileEntity = te;
        links = new ReplaceableItemHandler(dummyLinks, LINK_FILTER, 1);
        recipes = new ReplaceableItemHandler(dummyRecipes, RECIPE_FILTER, 1);
        layoutAll();
        setPage(0);
    }
    
    private void layoutAll() {
        layoutPlayerInventory();
        layoutLinkSlots();
        layoutPatternSlots();
    }
    
    private void layoutPatternSlots() {
        for(int i = 0; i < 42; i++){
            addSlotToContainer(new SlotItemHandler(recipes, i, patternX + 18 * (i % 6), patternY + 18 * (i / 6)));
        }
    }
    
    private void layoutLinkSlots() {
        for(int i = 0; i < 18; i++){
            addSlotToContainer(new SlotItemHandler(links, i, linkX + (i % 6) * 18, linkY + (i / 6) * 18));
        }
    }
    
    private void layoutPlayerInventory() {
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 9; col++){
                addSlotToContainer(
                        new Slot(player.inventory, col + row * 9 + 9, 48 + col * 18, ySize - 10 - (4 - row) * 18));
            }
        }
        
        for(int hotbar = 0; hotbar < 9; hotbar++){
            addSlotToContainer(new Slot(player.inventory, hotbar, 48 + hotbar * 18, ySize - 24));
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }
    
    public void setPage(int page) {
        if(tileEntity.getPageCount() > page) {
            currentPage = tileEntity.getPage(page);
            recipes.replaceItemList(currentPage.recipes);
            links.replaceItemList(currentPage.maschineLinks);
        }
    }
    
    public void createPage(String name) {
        currentPage = new RecipeBankTileEntity.RecipePage();
        currentPage.setName(name);
        recipes.replaceItemList(currentPage.recipes);
        links.replaceItemList(currentPage.maschineLinks);
        tileEntity.addPage(currentPage);
    }
    
    public void removePage() {
        //todo clear page
        if(currentPage == null) {
            return;
        }
        RecipeBankTileEntity.RecipePage old = currentPage;
        currentPage = null;
        tileEntity.removePage(old);
        if(!tileEntity.getWorld().isRemote) {
            World    world = tileEntity.getWorld();
            BlockPos pos   = tileEntity.getPos();
            for(ItemStack stack : old.recipes){
                if(!stack.isEmpty()) {
                    world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
                }
            }
            for(ItemStack stack : old.maschineLinks){
                if(!stack.isEmpty()) {
                    world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
                }
            }
        }
        links.replaceItemList(dummyLinks);
        recipes.replaceItemList(dummyRecipes);
    }
    
    public void renamePage(final String name) {
        if(currentPage != null) {
            currentPage.setName(name);
        }
    }
}
