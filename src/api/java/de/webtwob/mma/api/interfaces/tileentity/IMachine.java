package de.webtwob.mma.api.interfaces.tileentity;

import de.webtwob.mma.api.enums.MachineState;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @param T the Type of the implementing Class
 */
public interface IMachine <T extends IMachine> {

    MachineState getState();

    void setState(MachineState state);

    int getWaitTicks();

    void setWaitTicks(int tick);

    int getDefaultWaitTime();

    default void waitOrError(
            final List<String> descriptions, final List<Function<T, String>> functions
    ) {
        descriptions.clear();

        functions.stream()
                 .map(e -> e.apply((T) this))
                 .filter(Objects::nonNull)
                 .forEach(descriptions::add);

        if (descriptions.isEmpty()) {
            functions.clear();
            setState(MachineState.IDLE);
        } else {
            setWaitTicks(getDefaultWaitTime());
        }
    }

}
