package de.webtwob.mma.core.common.references;

import de.webtwob.mma.api.MMAAPI;
import de.webtwob.mma.api.registries.MultiBlockGroupType;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public class ObjectHolders {

    /* Blocks */

    @ObjectHolder("minecraft:furnace")
    public static final Block               FURNACE          = null;
    @ObjectHolder("minecraft:anvil")
    public static final Block               ANVIL            = null;
    @ObjectHolder("minecraft:iron_bars")
    public static final Block               IRON_BARS        = null;
    @ObjectHolder("minecraft:chest")
    public static final Block               CHEST            = null;
    @ObjectHolder("minecraft:crafting_table")
    public static final Block               CRAFTING_TABLE   = null;


    /* GroupTypes */

    @ObjectHolder("mmacore:crafting")
    public static final MultiBlockGroupType MANAGER_CRAFTING = null;
    @ObjectHolder("mmacore:recipes")
    public static final MultiBlockGroupType MANAGER_RECIPES = null;

    @Mod.Instance(value = "mmaapi", owner = "mmacore")
    public static MMAAPI apiInstance = null;//NOSONAR

    private ObjectHolders() {

    }

}
