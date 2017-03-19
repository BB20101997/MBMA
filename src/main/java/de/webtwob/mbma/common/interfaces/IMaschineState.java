package de.webtwob.mbma.common.interfaces;

import de.webtwob.mbma.api.enums.MaschineState;

import javax.annotation.Nonnull;

/**
 * Created by BB20101997 on 19. Mär. 2017.
 */
public interface IMaschineState {

    void setMaschineState(@Nonnull MaschineState state);

    @Nonnull
    MaschineState getMaschineState();
}
