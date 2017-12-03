package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.interfaces.capability.ICraftingRecipe;
import de.webtwob.mma.api.interfaces.capability.IPatternProvider;
import de.webtwob.mma.api.registries.MultiBlockGroupType;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityPatternStore extends MultiBlockTileEntity {
    
    @ObjectHolder("mmacore:recipes")
    public static final MultiBlockGroupType MANAGER_RECIPES = null;
    private static Capability<IPatternProvider> capabilityPatternProvider = null;
    @Nonnull
    private List<ItemStack> patternList = new ArrayList<>();
    
    @CapabilityInject(IPatternProvider.class)
    private static void setCapabilityPatternProvider(Capability<IPatternProvider> capabilityPatternProvider) {
        TileEntityPatternStore.capabilityPatternProvider = capabilityPatternProvider;
    }
    
    @Override
    public MultiBlockGroupType getGroupType() {
        return MANAGER_RECIPES;
    }
    
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == capabilityPatternProvider) {
            IPatternProvider patternProvider = () -> patternList.stream().map(ICraftingRecipe::getRecipeForStack).collect(Collectors.toList());
            return (T) patternProvider;
        }
        return super.getCapability(capability, facing);
    }
}
