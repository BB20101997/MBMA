package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.interfaces.capability.ICraftingRecipe;
import de.webtwob.mma.api.interfaces.capability.IPatternProvider;
import de.webtwob.mma.api.registries.MultiBlockGroupType;
import de.webtwob.mma.core.common.references.CapabilityInjections;
import de.webtwob.mma.core.common.references.ObjectHolders;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityPatternStore extends MultiBlockTileEntity {

    @Nonnull
    private             List<ItemStack>     patternList     = new ArrayList<>();

    @Override
    public MultiBlockGroupType getGroupType() {
        return ObjectHolders.MANAGER_RECIPES;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        Capability<IPatternProvider> patternProviderCapability = CapabilityInjections.getCapabilityPatternProvider();
        if (patternProviderCapability != null && capability == patternProviderCapability) {
            IPatternProvider patternProvider = () -> patternList.stream()
                                                                .map(ICraftingRecipe::getRecipeForStack)
                                                                .collect(Collectors.toList());
            return (T) patternProvider;
        }
        return super.getCapability(capability, facing);
    }
}
