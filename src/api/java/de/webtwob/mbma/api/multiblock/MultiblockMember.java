package de.webtwob.mbma.api.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class MultiblockMember {
    private final BlockPos pos;
    private final int worldId;
    
    public MultiblockMember(BlockPos pos, int worldId) {
        this.pos = pos.toImmutable();
        this.worldId = worldId;
    }
    
    public MultiblockMember(BlockPos pos, World world){
        this(pos,world.provider.getDimension());
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
        if (obj != null && obj instanceof MultiblockMember) {
            MultiblockMember member = (MultiblockMember) obj;
            return worldId == member.worldId && pos.equals(member.pos);
        } else {
            return false;
        }
    }
}
