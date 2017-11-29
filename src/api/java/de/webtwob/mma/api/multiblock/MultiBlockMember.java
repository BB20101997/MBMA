package de.webtwob.mma.api.multiblock;

import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class MultiBlockMember implements INBTSerializable<NBTTagIntArray>{
    private BlockPos pos;
    private int worldId;

    /**
     * @param dimensionID the world id of the world the member is in
     * @param pos the position this member is at in worldId
     * */
    public MultiBlockMember(int dimensionID, BlockPos pos) {
        this.pos = pos.toImmutable();
        this.worldId = dimensionID;
    }

    /**
     * @param world the world the member is in, used to get the dimensionId
     * @param pos the position this member is at in worldId
     * */
    public MultiBlockMember(World world, BlockPos pos){
        this(world.provider.getDimension(), pos);
    }

    /**
     * used to create Members for deserializing NBT
     * */
    public MultiBlockMember() {

    }

    public BlockPos getPos() {
        return pos;
    }

    public int getWorldId() {
        return worldId;
    }

    @Override
    public int hashCode() {
        return pos.hashCode() + worldId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MultiBlockMember) {
            MultiBlockMember member = (MultiBlockMember) obj;
            return worldId == member.worldId && pos.equals(member.pos);
        } else {
            return false;
        }
    }

    @Override
    public NBTTagIntArray serializeNBT() {
        if(pos!=null){
            return new NBTTagIntArray(new int[]{pos.getX(),pos.getY(),pos.getZ(),worldId});
        }
        return new NBTTagIntArray(new int[]{worldId});
    }

    @Override
    public void deserializeNBT(NBTTagIntArray nbt) {
        int[] coords = nbt.getIntArray();
        if(coords.length>3){
            pos = new BlockPos(coords[0],coords[1],coords[2]);
            worldId = coords[3];
        }else if(coords.length>0){
            worldId = coords[0];
        }
    }
}
