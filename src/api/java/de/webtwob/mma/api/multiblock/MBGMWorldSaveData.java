package de.webtwob.mma.api.multiblock;

import de.webtwob.mma.api.references.NBTKeys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mma.api.MMAAPI.MODID;

/**
 * Created by BB20101997 on 31. Okt. 2017.
 */
@Mod.EventBusSubscriber(modid = MODID)
public class MBGMWorldSaveData extends WorldSavedData {

    private static final String                 DATA_NAME              = MODID + "_MBGMData";
    public final         MultiBlockGroupManager multiBlockGroupManager = new MultiBlockGroupManager(this);

    public MBGMWorldSaveData() {
        this(DATA_NAME);
    }

    public MBGMWorldSaveData(String name) {
        super(name);
    }

    @Nullable
    public static MBGMWorldSaveData get(World world) {
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
        if (nbt.hasKey(NBTKeys.MBG_LIST, Constants.NBT.TAG_LIST)) {
            multiBlockGroupManager.deserializeNBT(nbt.getTagList(NBTKeys.MBG_LIST, Constants.NBT.TAG_COMPOUND));
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        compound.setTag(NBTKeys.MBG_LIST, multiBlockGroupManager.serializeNBT());
        return compound;
    }

}
