package de.webtwob.mbma.api.multiblock;

import de.webtwob.mbma.api.MBMA_API_Constants;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mbma.api.MBMA_API.MODID;


/**
 * Created by BB20101997 on 31. Okt. 2017.
 */
@Mod.EventBusSubscriber(modid = MODID)
public class MBGMWorldSaveData extends WorldSavedData {
    
    private static final String DATA_NAME = MODID + "_MBGMData";
    public final MultiBlockGroupManager multiBlockGroupManager = new MultiBlockGroupManager(this);
    
    public MBGMWorldSaveData() {
        super(DATA_NAME);
    }
    
    @Nullable
    public static MBGMWorldSaveData get(World world) throws NullPointerException {
        MapStorage storage = world.getMapStorage();
        if (storage == null) {
            return null;
        }
        MBGMWorldSaveData data = (MBGMWorldSaveData) storage.getOrLoadData(MBGMWorldSaveData.class, DATA_NAME);
        
        if (data == null) {
            data = new MBGMWorldSaveData();
            storage.setData(DATA_NAME, data);
        }
        return data;
    }
    
    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        if (nbt.hasKey(MBMA_API_Constants.NBT.MBG_LIST, Constants.NBT.TAG_LIST)) {
            multiBlockGroupManager.deserializeNBT(nbt.getTagList(MBMA_API_Constants.NBT.MBG_LIST, Constants.NBT.TAG_COMPOUND));
        }
    }
    
    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        compound.setTag(MBMA_API_Constants.NBT.MBG_LIST, multiBlockGroupManager.serializeNBT());
        return compound;
    }
    
}
