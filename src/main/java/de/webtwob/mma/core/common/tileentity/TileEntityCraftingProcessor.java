package de.webtwob.mma.core.common.tileentity;

import de.webtwob.mma.api.enums.MachineState;
import de.webtwob.mma.api.interfaces.tileentity.IMachine;
import de.webtwob.mma.api.registries.MultiBlockGroupType;
import de.webtwob.mma.core.common.config.MMAConfiguration;
import de.webtwob.mma.core.common.references.ObjectHolders;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by BB20101997 on 25. Okt. 2017.
 */
public class TileEntityCraftingProcessor extends MultiBlockTileEntity implements IMachine<TileEntityCraftingProcessor> {

    private final List<Function<TileEntityCraftingProcessor, String>> errorSolved   = new LinkedList<>();
    private final List<Function<TileEntityCraftingProcessor, String>> waitCondition = new LinkedList<>();
    private final List<String>                                        errors        = new LinkedList<>();
    private final List<String>                                        waiting       = new LinkedList<>();
    private       MachineState                                        state         = MachineState.IDLE;
    private int pause;

    @Override
    public MultiBlockGroupType getGroupType() {
        return ObjectHolders.MANAGER_CRAFTING;
    }

    @Override
    public void update() {
        super.update();
        if (0 <= pause) {
            switch (state) {

                case IDLE:
                    //Wait till the controller assignees us a task
                    pause = getDefaultWaitTime();
                    break;
                case RUNNING:
                    //TODO
                    break;
                case WAITING:
                    waitOrError(waiting, waitCondition);
                    break;
                case PROBLEM:
                    waitOrError(errors, errorSolved);
                    break;
            }
        }else{
            pause--;
        }
    }

    @Override
    public MachineState getState() {
        return state;
    }

    @Override
    public void setState(final MachineState state) {
        this.state = state;
    }

    @Override
    public int getWaitTicks() {
        return pause;
    }

    @Override
    public void setWaitTicks(final int tick) {
        pause = tick;
    }

    @Override
    public int getDefaultWaitTime() {
        return MMAConfiguration.defaultWaitTicks;
    }
}
