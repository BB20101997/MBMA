package de.webtwob.mbma.api.property;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.IBlockPosProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by bennet on 26.03.17.
 */
public class IsLinkedItemPropertyGetter implements IItemPropertyGetter {
    public static final IsLinkedItemPropertyGetter INSTANCE = new IsLinkedItemPropertyGetter();

    @Override
    public float apply(ItemStack itemStack, @Nullable World world, @Nullable EntityLivingBase entityLivingBase) {

        IBlockPosProvider bpp = itemStack.getCapability(APICapabilities.CAPABILITY_BLOCK_POS, null);
        return bpp != null && bpp.getBlockPos() != null ? 1 : 0;
    }
}
