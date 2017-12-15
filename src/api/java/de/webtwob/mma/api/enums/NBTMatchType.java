package de.webtwob.mma.api.enums;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 11. Nov. 2017.
 */
public enum NBTMatchType {
    /**
     * The Set of NBTTags of the Template and Set of NBTTags of the ItemStack to be used must be identical
     * and all NBTTags must match
     * NBTTagList elements must also be in the same order
     */
    FULL {
        @Override
        boolean compareNBT(@Nonnull NBTBase template, @Nonnull NBTBase toMatch) {
            return template.equals(toMatch);
        }
    }, /**
     * The Set of NBTTags of the Template must be a Sub-Set of the Set of NBTTags of the ItemStack to be used
     * and all NBTTag values of the Template must match the NBTTag values of the NBTTagSet of the ItemStack to be used
     */
    PARTIALL {
                @Override
                boolean compareNBT(@Nonnull NBTBase template, @Nonnull NBTBase toMatch) {
                    return NBTUtil.areNBTEquals(template, toMatch, true);
                }
            }, PARTIALL_NO_LIST {
        @Override
        boolean compareNBT(@Nonnull NBTBase template, @Nonnull NBTBase toMatch) {
            return NBTUtil.areNBTEquals(template, toMatch, false);
        }
    }, /**
     * The NBTTag-Set of the template and the NBTTag-Set of the ItemStack to be used will not be compared
     */
    IGNORE {
                @Override
                boolean compareNBT(@Nonnull NBTBase template, @Nonnull NBTBase toMatch) {
                    return true;
                }
            };

    abstract boolean compareNBT(@Nonnull NBTBase template, @Nonnull NBTBase toMatch);
}
