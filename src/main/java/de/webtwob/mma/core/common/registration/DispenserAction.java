package de.webtwob.mma.core.common.registration;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * Created by BB20101997 on 04. Dez. 2017.
 */
public class DispenserAction {

    /**
     * registers the Cores DispenserBehavior
     */
    public void registerDispenserBehavior() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.IN_WORLD_CRAFTER, (source, stack) -> {
            IBlockState state = source.getBlockState();
            if (state.getPropertyKeys().contains(BlockDispenser.FACING)) {
                EnumFacing facing = state.getValue(BlockDispenser.FACING);
                //use the InWorldCrafter on the block in front of the dispenser
                Items.IN_WORLD_CRAFTER.onItemUse(null, source.getWorld(), source.getBlockPos().offset(facing), null,
                                                 facing, 0.5f, 0.5f, 0.5f
                );
            }
            return ItemStack.EMPTY;
        });
    }

}
