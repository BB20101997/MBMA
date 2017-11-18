package de.webtwob.mbma.core.common.tileentity;

import de.webtwob.mbma.api.crafting.ItemStackContainer;
import de.webtwob.mbma.api.registries.MultiBlockGroupType;
import de.webtwob.mbma.core.common.config.MBMAConfiguration;
import de.webtwob.mbma.core.common.references.NBTKeys;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityCraftingStorage extends MultiBlockTileEntity {
    
    @ObjectHolder("mbmacore:crafting")
    public static final MultiBlockGroupType MANAGER_CRAFTING = null;
    public final List<ItemStackContainer> containerList;
    
    /**
     * Creates a new TileEntityCraftingStorage with a fixed Size unmodifiable {@literal List<ItemStackContainer>}
     *     with size based on the value found in the configuration file
     */
    public TileEntityCraftingStorage(){
        ItemStackContainer[] container = new ItemStackContainer[MBMAConfiguration.storageStackLimit];
        Arrays.setAll(container, i -> new ItemStackContainer());
        containerList = Collections.unmodifiableList(Arrays.asList(container));
    }
    
    @Override
    public MultiBlockGroupType getGroupType() {
        return MANAGER_CRAFTING;
    }
    
    @Override
    public void update() {
        super.update();
        //TODO
    }
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbtc) {
        NBTTagCompound compound = super.writeToNBT(nbtc);
        NBTTagList itemList = new NBTTagList();
        for (ItemStackContainer isc : containerList) {
            ItemStack stack = isc.getItemStack();
            if (!stack.isEmpty()) {
                itemList.appendTag(stack.serializeNBT());
            }
        }
        compound.setTag(NBTKeys.CRAFTING_STORAGE_LIST, itemList);
        return compound;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList itemList = compound.getTagList(NBTKeys.CRAFTING_STORAGE_LIST, Constants.NBT.TAG_COMPOUND);
        int sizeNBT = itemList.tagCount();
        int sizeList = containerList.size();
        for (int i = 0; i < sizeList && i < sizeNBT; i++) {
            containerList.get(i).setItemStack(new ItemStack(itemList.getCompoundTagAt(i)));
        }
        if (sizeNBT > sizeList) {
            for (int i = sizeNBT; i < sizeList; i++) {
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(itemList.getCompoundTagAt(i))));
            }
        }
    }
}
