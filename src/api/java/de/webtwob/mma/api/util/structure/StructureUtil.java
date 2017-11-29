package de.webtwob.mma.api.util.structure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

/**
 * Created by BB20101997 on 12. Apr. 2017.
 */
public class StructureUtil {

    private StructureUtil() {
    }

    /**
     * A helper function for placing Structures
     *
     * @param pos      the position the structure should be placed at
     * @param world    the world to place the structure in
     * @param loc      the ResourceLocation for the structure file
     * @param settings the PlacementSettings to use when placing this structure
     * @return true if we succeeded in placing the structure
     */
    public static boolean placeStructureInWorld(BlockPos pos, World world, ResourceLocation loc, PlacementSettings
            settings) {
        if (!world.isRemote && world instanceof WorldServer) {
            WorldServer worldServer = (WorldServer) world;
            Template tmp = worldServer.getStructureTemplateManager().get(worldServer.getMinecraftServer(),
                    loc);
            if (tmp == null) {
                return false;
            }
            tmp.addBlocksToWorldChunk(world, pos, settings);
            return true;
        }
        return false;
    }

}
