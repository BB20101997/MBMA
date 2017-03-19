package de.webtwob.mbma.api;

import de.webtwob.mbma.api.enums.MaschineState;
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
    public static final PropertyDirection           FACING    = PropertyDirection.create("facing");
    public static final PropertyBool                CONNECTED = PropertyBool.create("connected");
    public static final PropertyEnum<MaschineState> STATE     = PropertyEnum.create("state", MaschineState.class);

    private MBMAProperties() {}
}
