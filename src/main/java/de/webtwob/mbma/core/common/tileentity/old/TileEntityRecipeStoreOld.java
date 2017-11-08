package de.webtwob.mbma.core.common.tileentity.old;

import de.webtwob.mbma.core.common.crafting.RecipePage;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static de.webtwob.mbma.core.common.references.MBMA_NBTKeys.RB_PAGES;
import static net.minecraftforge.common.util.Constants.NBT;

/**
 * Created by bennet on 01.04.17.
 */
@Deprecated
public class TileEntityRecipeStoreOld extends TileEntity {

    private NonNullList<RecipePage> pages = NonNullList.create();

    public void addPage(final RecipePage recipePage) {
        pages.add(recipePage);
        markDirty();
    }

    public void removePage(final RecipePage currentPage) {
        pages.remove(currentPage);
        markDirty();
    }

    public void destroyed() {
        World world = getWorld();
        BlockPos pos = getPos();
        for (RecipePage page : pages) {
            for (ItemStack stack : page.maschineLinks) {
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
            }
            for (ItemStack stack : page.recipes) {
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
            }
        }
    }

    public List<RecipePage> getPages() {
        return pages;
    }

    public int getPageCount() {
        return pages.size();
    }

    public RecipePage getPage(int i) {
        return pages.get(i);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound p_writeToNBT_1_) {
        NBTTagCompound compound = getTileData();
        NBTTagList pageList = new NBTTagList();
        for (RecipePage page : pages) {
            pageList.appendTag(page.serializeNBT());
        }
        compound.setTag(RB_PAGES, pageList);
        return super.writeToNBT(p_writeToNBT_1_);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        NBTTagCompound compound = getTileData();
        if (compound.hasKey(RB_PAGES, NBT.TAG_LIST)) {
            NBTTagList pageList = compound.getTagList(RB_PAGES, NBT.TAG_COMPOUND);
            RecipePage page;
            for (int i = 0; i < pageList.tagCount(); i++) {
                page = new RecipePage();
                page.deserializeNBT(pageList.getCompoundTagAt(i));
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
