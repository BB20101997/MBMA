package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

import static de.webtwob.mbma.common.references.MBMA_NBTKeys.RB_PAGE_LINK;
import static de.webtwob.mbma.common.references.MBMA_NBTKeys.RB_PAGE_NAME;
import static de.webtwob.mbma.common.references.MBMA_NBTKeys.RB_PAGE_RECIPE;

/**
 * Created by bennet on 01.04.17.
 */
public class RecipeBankTileEntity extends TileEntity {
    
    private NonNullList<RecipePage> pages = NonNullList.create();
    
    public void addPage(final RecipePage recipePage) {
        pages.add(recipePage);
        markDirty();
    }
    
    public void removePage(final int pageId) {
        pages.remove(pageId);
        markDirty();
    }
    
    public void removePage(final RecipePage currentPage) {
        pages.remove(currentPage);
        markDirty();
    }
    
    public static class RecipePage implements INBTSerializable<NBTTagCompound> {
        
        public NonNullList<ItemStack> recipes       = NonNullList.withSize(42, ItemStack.EMPTY);
        public NonNullList<ItemStack> maschineLinks = NonNullList.withSize(18,ItemStack.EMPTY);
        
        public String name;
        
        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound comp = new NBTTagCompound();
            if(name != null) {
                comp.setString(RB_PAGE_NAME, name);
            }
            int            itemIndex = 0;
            NBTTagCompound rec       = new NBTTagCompound();
            for(ItemStack stack : recipes){
                rec.setTag(RB_PAGE_RECIPE + itemIndex++, stack.serializeNBT());
            }
            comp.setTag(RB_PAGE_RECIPE + "s", rec);
            rec = new NBTTagCompound();
            itemIndex = 0;
            for(ItemStack stack : maschineLinks){
                rec.setTag(RB_PAGE_LINK + itemIndex++, stack.serializeNBT());
            }
            comp.setTag(RB_PAGE_LINK + "s", rec);
            return comp;
        }
        
        @Override
        public void deserializeNBT(NBTTagCompound compound) {
            if(compound.hasKey(RB_PAGE_NAME)) {
                name = compound.getString(RB_PAGE_NAME);
            }
            NBTTagCompound rec;
            if(compound.hasKey(RB_PAGE_RECIPE + "s")) {
                rec = compound.getCompoundTag(RB_PAGE_RECIPE + "s");
                for(String key : rec.getKeySet()){
                    recipes.add(new ItemStack(rec.getCompoundTag(key)));
                }
            }
            if(compound.hasKey(RB_PAGE_LINK + "s")) {
                rec = compound.getCompoundTag(RB_PAGE_LINK + "s");
                for(String key : rec.getKeySet()){
                    maschineLinks.add(new ItemStack(rec.getCompoundTag(key)));
                }
            }
        }
        
        public void setName(final String name) {
            this.name = name;
        }
    }
    
    public int getPageCount() {
        return pages.size();
    }
    
    public RecipePage getPage(int i) {
        return pages.get(i);
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound p_writeToNBT_1_) {
        NBTTagCompound compound  = getTileData();
        int            pageIndex = 0;
        for(RecipePage page : pages){
            compound.setTag(MBMA_NBTKeys.RB_PAGES + pageIndex++, page.serializeNBT());
        }
        compound.setInteger(MBMA_NBTKeys.RB_PAGES + "SIZE", pageIndex);
        return super.writeToNBT(p_writeToNBT_1_);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound compound = getTileData();
        int        size = compound.getInteger(MBMA_NBTKeys.RB_PAGES + "SIZE");
        RecipePage page;
        for(int i = 0; i < size; i++){
            if(compound.hasKey(MBMA_NBTKeys.RB_PAGES + i)) {
                page = new RecipePage();
                page.deserializeNBT(compound.getCompoundTag(MBMA_NBTKeys.RB_PAGES + i));
                pages.add(page);
            }
        }
    }
    
    @Override
    public void onDataPacket(final NetworkManager networkManager, final SPacketUpdateTileEntity packet) {
        super.onDataPacket(networkManager, packet);
        readFromNBT(packet.getNbtCompound());
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(),0,writeToNBT(new NBTTagCompound()));
    }
}
