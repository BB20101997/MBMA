package de.webtwob.mbma.common.tileentity;

import de.webtwob.mbma.MBMA_NBTKeys;
import de.webtwob.mbma.api.MBMAProperties;
import de.webtwob.mbma.common.MBMALog;
import de.webtwob.mbma.common.MBMAPacketHandler;
import de.webtwob.mbma.common.packet.PSIBStatePacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class PSITileEntity extends TileEntity {

    public PSITileEntity() {}

    public boolean isConnected() {
        return getTileData().getBoolean(MBMA_NBTKeys.PSIConnected);
    }

    public void setConnected(boolean connected) {

        getTileData().setBoolean(MBMA_NBTKeys.PSIConnected,connected);
        if(hasWorld()) {
            if(!getWorld().isRemote) {
                MBMAPacketHandler.INSTANCE.sendToDimension(new PSIBStatePacket(pos, connected), getWorld().provider
                                                                                                        .getDimension
                                                                                                                 ());
            }else{
                getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
            }
        }
        markDirty();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        MBMALog.debug("PSITileEntity creating SPacketUpdateTileEntity");
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        MBMALog.debug("SPacketUpdateTileEntity in PSITileEntity received!");
        readFromNBT(pkt.getNbtCompound());
    }

    public void update() {
        if(hasWorld()) {
            World world = getWorld();
            if(!world.isRemote) {
                BlockPos   pos         = getPos();
                EnumFacing orientation = world.getBlockState(pos).getValue(MBMAProperties.FACING);
                BlockPos   dest        = pos.offset(orientation);
                TileEntity te          = world.getTileEntity(dest);
                if(te != null) {
                    IItemHandler capability = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                                                               orientation.getOpposite());
                    setConnected(capability != null);
                } else {
                    setConnected(false);
                }
            }
        }
    }
}
