package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.registries.MultiBlockGroupType;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityPatternStore extends MultiBlockTileEntity {

    @Nonnull
    private List<ItemStack> patternList = new ArrayList<>();


    @ObjectHolder("mmacore:recipes")
    public static final MultiBlockGroupType MANAGER_RECIPES = null;

    @Override
    public MultiBlockGroupType getGroupType() {
        return MANAGER_RECIPES;
    }

    @Nonnull
    public List<ItemStack> getPatternList() {
        return Collections.unmodifiableList(patternList);
    }
}
