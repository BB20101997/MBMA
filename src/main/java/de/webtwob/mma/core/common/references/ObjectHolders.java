package de.webtwob.mma.core.common.references;

import de.webtwob.mma.api.MMAAPI;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public class ObjectHolders {
    
    @ObjectHolder("minecraft:furnace")
    public static final Block FURNACE        = null;
    @ObjectHolder("minecraft:anvil")
    public static final Block ANVIL          = null;
    @ObjectHolder("minecraft:iron_bars")
    public static final Block IRON_BARS      = null;
    @ObjectHolder("minecraft:chest")
    public static final Block CHEST          = null;
    @ObjectHolder("minecraft:crafting_table")
    public static final Block CRAFTING_TABLE = null;
   
    @Mod.Instance(value = "mmaapi",owner = "mmacore")
    public static MMAAPI apiInstance = null;//NOSONAR
    
    private ObjectHolders() {
    
    }
    
}
