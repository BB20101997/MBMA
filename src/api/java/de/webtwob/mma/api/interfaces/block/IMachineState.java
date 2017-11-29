package de.webtwob.mma.api.interfaces.block;

import de.webtwob.mma.api.enums.MachineState;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public interface IMachineState {

    @Nonnull
    MachineState getMachineState();

    void setMachineState(@Nonnull MachineState state);
}
