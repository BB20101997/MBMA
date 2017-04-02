package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.api.RecipePage;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
    
    public void destroyed() {
        World    world = getWorld();
        BlockPos pos   = getPos();
        for(RecipePage page : pages){
           for(ItemStack stack: page.maschineLinks){
               world.spawnEntity(new EntityItem(world,pos.getX(),pos.getY(),pos.getZ(),stack));
           }
           for(ItemStack stack: page.recipes){
               world.spawnEntity(new EntityItem(world,pos.getX(),pos.getY(),pos.getZ(),stack));
           }
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
        int            size     = compound.getInteger(MBMA_NBTKeys.RB_PAGES + "SIZE");
        RecipePage     page;
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
        return new SPacketUpdateTileEntity(getPos(), 0, writeToNBT(new NBTTagCompound()));
    }
}
