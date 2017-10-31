package de.webtwob.mbma.core.common.crafting.old;

import de.webtwob.mbma.api.capability.implementations.CombinedItemHandler;
import de.webtwob.mbma.api.capability.implementations.FilteredItemHandler;
import de.webtwob.mbma.api.enums.MachineState;
import de.webtwob.mbma.api.interfaces.capability.IBlockPosProvider;
import de.webtwob.mbma.api.property.MBMAProperties;
import de.webtwob.mbma.api.util.MBMAFilter;
import de.webtwob.mbma.core.common.tileentity.old.TileEntityQueueOld;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static de.webtwob.mbma.core.common.references.MBMA_NBTKeys.*;

/**
 * Created by BB20101997 on 07. Mai. 2017.
 */
@Deprecated
public class QSData implements INBTSerializable {

    final TileEntityQueueOld tileEntityQueue;

    final NonNullList<ItemStack> permLinkList = NonNullList.withSize(6, ItemStack.EMPTY);
    final NonNullList<ItemStack> tempLinkList = NonNullList.withSize(6, ItemStack.EMPTY);
    public final NonNullList<ItemStack> recipeList = NonNullList.withSize(6, ItemStack.EMPTY);
    public final NonNullList<ItemStack> collectList = NonNullList.create();
    public final ItemStackHandler permLinks = new FilteredItemHandler(permLinkList, MBMAFilter.LINK_FILTER, 1) {
        @Override
        protected void onContentsChanged(int slot) {
            tileEntityQueue.markDirty();
        }
    };
    public final ItemStackHandler tempLinks = new FilteredItemHandler(tempLinkList, MBMAFilter.LINK_FILTER, 1) {
        @Override
        protected void onContentsChanged(int slot) {
            tileEntityQueue.markDirty();
        }
    };
    final ItemStackHandler recipesLinks = new FilteredItemHandler(recipeList, MBMAFilter.LINK_FILTER, 1) {
        @Override
        protected void onContentsChanged(int slot) {
            tileEntityQueue.markDirty();
        }
    };
    public final ItemStackHandler combinedLinks = new CombinedItemHandler(permLinks, tempLinks, recipesLinks);
    @Nonnull
    public
    ItemStack token = ItemStack.EMPTY;
    @Nonnull
    public
    MachineState machineState = MachineState.PROBLEM;

    public QSData(TileEntityQueueOld entity) {
        tileEntityQueue = entity;
    }

    @Override
    public NBTBase serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger(QS_STATE, machineState.ordinal());

        compound.setTag(QS_TOKEN, token.serializeNBT());

        compound.setInteger(QS_COLLECT_LENGTH, collectList.size());
        compound.setTag(QS_COLLECT_LIST, new ItemStackHandler(collectList).serializeNBT());

        compound.setTag(QS_ITEM_HANDLER, combinedLinks.serializeNBT());

        return compound;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        if (nbt instanceof NBTTagCompound) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            if (compound.hasKey(QS_STATE, NBT.TAG_INT)) {
                machineState = MachineState.values()[compound.getInteger(QS_STATE)];
            }
            if (compound.hasKey(QS_TOKEN, NBT.TAG_COMPOUND)) {
                token = new ItemStack(compound.getCompoundTag(QS_TOKEN));
            }
            if (compound.hasKey(QS_COLLECT_LENGTH, NBT.TAG_INT) && compound.hasKey(QS_COLLECT_LIST)) {
                int size = compound.getInteger(QS_COLLECT_LENGTH);
                while (collectList.size() < size) {
                    collectList.add(ItemStack.EMPTY);
                }
                new ItemStackHandler(collectList).deserializeNBT(compound.getCompoundTag(QS_COLLECT_LIST));
            }
            if (compound.hasKey(QS_ITEM_HANDLER, NBT.TAG_COMPOUND)) {
                combinedLinks.deserializeNBT(compound.getCompoundTag(QS_ITEM_HANDLER));
            }
        }
    }

    @Nullable
    public IItemHandler getInventoryFromLink(ItemStack itemStack, World world) {
        BlockPos pos = IBlockPosProvider.getBlockPos(itemStack);
        if (pos != null) {
            IBlockState state = world.getBlockState(pos);

            if (!state.getProperties().containsKey(MBMAProperties.CONNECTED)) {
                return null;
            }
            if (state.getValue(MBMAProperties.CONNECTED)) {
                EnumFacing dir = state.getValue(MBMAProperties.FACING);
                TileEntity te = world.getTileEntity(pos.offset(dir));
                if (te != null) {
                    return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite());
                }
            }
        }
        return null;
    }
}
