package de.webtwob.mma.api.enums;

import net.minecraft.util.IStringSerializable;

/**
 * Created by BB20101997 on 18. Mär. 2017.
 */
public enum MachineState implements IStringSerializable {
    IDLE, RUNNING, WAITING, PROBLEM;

    @Override
    public String getName() {
        return super.name().toLowerCase();
    }
}
