package de.webtwob.mbma.api.property;

import de.webtwob.mbma.api.enums.MachineState;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
public class MBMAProperties {

    /*
     * Note: Names have to be lower-case
     * */
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool CONNECTED = PropertyBool.create("connected");
    public static final PropertyEnum<MachineState> STATE = PropertyEnum.create("state", MachineState.class);

    private MBMAProperties() {
    }
}
