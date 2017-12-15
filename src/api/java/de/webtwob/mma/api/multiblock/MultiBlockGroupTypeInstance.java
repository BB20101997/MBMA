package de.webtwob.mma.api.multiblock;

import javax.annotation.Nullable;

/**
 * Created by BB20101997 on 10. Dez. 2017.
 */
public interface MultiBlockGroupTypeInstance {

    /**
     * You are the winning party of the join either migrate oldInstances data into your data or call their lostOnJoin function
     */
    void joinData(@Nullable MultiBlockGroupTypeInstance oldInstance);

    /**
     * You lost on a join and your data couldn't be migrated in to the winning party's data
     */
    void lostOnJoin();

    default void onAdd(MultiBlockMember member) {}

}
