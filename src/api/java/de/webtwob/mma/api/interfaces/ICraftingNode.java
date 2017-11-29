package de.webtwob.mma.api.interfaces;

import de.webtwob.mma.api.exceptions.CraftingException;

import java.util.Optional;

/**
 * Created by BB20101997 on 26. Okt. 2017.
 */
public interface ICraftingNode {

    void pushItemStackDown(int stackID);

    Optional<ICraftingNode> cycle() throws CraftingException;
}
