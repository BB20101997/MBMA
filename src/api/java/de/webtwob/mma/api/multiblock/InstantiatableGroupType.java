package de.webtwob.mma.api.multiblock;

import de.webtwob.mma.api.registries.MultiBlockGroupType;

import java.util.function.Supplier;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
public abstract class InstantiatableGroupType <I extends MultiBlockGroupTypeInstance> extends MultiBlockGroupType {

    public abstract I createGroupTypeInstance(MultiBlockGroup group, Supplier<Void> markDirtyCallback);

}
