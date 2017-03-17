package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.common.capability.QSItemHandler;
import de.webtwob.mbma.common.references.MBMA_NBTKeys;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * Created by bennet on 17.03.17.
 */
public class QSTileEntity extends TileEntity{

    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> ITEM_HANDLER = null;

    private final QSItemHandler itemHandler = new QSItemHandler(this);

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(ITEM_HANDLER!=null&&capability==ITEM_HANDLER){
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(ITEM_HANDLER!=null&&capability==ITEM_HANDLER){
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound td = getTileData();
        td.setTag(MBMA_NBTKeys.QSITEMHANDLER, ITEM_HANDLER.getStorage().writeNBT(ITEM_HANDLER, itemHandler, null));
        compound =  super.writeToNBT(compound);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound td = getTileData();
        ITEM_HANDLER.getStorage().readNBT(ITEM_HANDLER,itemHandler,null,td.getTag(MBMA_NBTKeys.QSITEMHANDLER));
    }
}
