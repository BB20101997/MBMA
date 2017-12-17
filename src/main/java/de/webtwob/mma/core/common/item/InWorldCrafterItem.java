package de.webtwob.mma.core.common.item;

import de.webtwob.mma.api.registries.InWorldRecipe;

import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 04. Dez. 2017.
 */
public class InWorldCrafterItem extends MMAItem {

    public InWorldCrafterItem(ResourceLocation rl) {
        super(rl);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn != null && pos != null && facing != null && !worldIn.isRemote) {
            IForgeRegistry<InWorldRecipe> registry = GameRegistry.findRegistry(InWorldRecipe.class);
            if (registry != null) {
                return tryFindRecipe(registry, worldIn, pos, facing);
            }
            return EnumActionResult.PASS;
        }
        return EnumActionResult.SUCCESS;
    }

    @Nonnull
    private EnumActionResult tryFindRecipe(@Nonnull IForgeRegistry<InWorldRecipe> registry, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing) {
        for (InWorldRecipe recipe : registry.getValues()) {
            BlockPattern               pattern = recipe.getBlockPattern();
            BlockPattern.PatternHelper helper  = pattern.match(worldIn, pos);
            if (helper != null) {
                recipe.executeRecipe(worldIn, helper);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean canItemEditBlocks() {
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return true;
    }
}
