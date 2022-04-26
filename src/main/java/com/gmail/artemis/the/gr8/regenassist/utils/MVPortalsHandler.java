package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import com.onarandombox.MultiversePortals.utils.PortalManager;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.List;

public class MVPortalsHandler {

    //--> keep in mind that the portalManager might be null
    private final MultiversePortals mvpAPI;
    private final PortalManager portalManager;
    private final Main plugin;
    private MVPortal portal;

    public MVPortalsHandler(MultiversePortals mvp, PortalManager pm, Main p) {
        mvpAPI = mvp;
        portalManager = pm;
        plugin = p;
    }

    //if a portal is present, select it by saving it as a private property and (re)setting createdNewPortalPlatform to false
    public boolean portalFound(String worldName) {
        if (portalManager != null) {
            List<MVPortal> portals = portalManager.getAllPortals();

            for (MVPortal p : portals) {
                if (p.getLocation().getMVWorld().getName().equalsIgnoreCase(worldName)) {
                    portal = p;
                    return true;
                }
            }
        }
        return false;
    }

    //print a new portal structure, and move the earlier found MVPortal to it
    //save the y-level to set players to spawn at as a private property
    public int relocateFoundPortal(String worldName, boolean useVanillaSpawn) {
        if (portalManager != null && portal != null) {
            World world = Bukkit.getServer().getWorld(worldName);
            if (world != null) {
                int spawnPlatformY = createPortalStructure(world, useVanillaSpawn);
                int portalBottom = spawnPlatformY+2;
                int portalTop = portalBottom+2;
                String mvportalLocation = "2.0," + portalBottom + ".0,3.0:3.0," + portalTop + ".0,3.0";
                portal.setPortalLocation(mvportalLocation, worldName);
                plugin.getLogger().info("Saving new portal location to the Multiverse-Portals config and reloading config");
                mvpAPI.savePortalsConfig();
                mvpAPI.reloadConfigs();
                portal = null;
                return spawnPlatformY+1;
            }
        }
        return 500;
    }

    //if a to-be-relocated portal has been found (and has NOT been moved yet), this will return the name of said portal
    public String getUnmovedFoundPortalName() {
        return portal != null ? portal.getName() : null;
    }

    //load the chunks around (0,0), print all the different portal parts at spawn, and return the y level the platform has been printed on
    private int createPortalStructure(World world, boolean useVanillaSpawn) {
        Location zero = new Location(world, 0, 0, 0);
        plugin.getLogger().info("Distance from spawn to 0, 0, 0: " + world.getSpawnLocation().distance(zero));
        plugin.getLogger().info("This is " + world.getSpawnLocation().distance(zero)/16 + " chunks away");

        plugin.getLogger().info("WorldName: " + world.getName());
        plugin.getLogger().info("Highest Motion_Blocking: " + world.getHighestBlockAt(0, 0, HeightMap.MOTION_BLOCKING).getType() + " at: " + world.getHighestBlockYAt(0, 0, HeightMap.MOTION_BLOCKING));
        plugin.getLogger().info("Highest Motion_Blocking_No_Leaves: " + world.getHighestBlockAt(0, 0, HeightMap.MOTION_BLOCKING_NO_LEAVES).getType() + " at: " + world.getHighestBlockYAt(0, 0, HeightMap.MOTION_BLOCKING_NO_LEAVES));
        plugin.getLogger().info("Highest Ocean_Floor: " + world.getHighestBlockAt(0, 0, HeightMap.OCEAN_FLOOR).getType() + " at: " + world.getHighestBlockYAt(0, 0, HeightMap.OCEAN_FLOOR));
        plugin.getLogger().info("Highest Ocean_Floor_World_Gen: " + world.getHighestBlockAt(0, 0, HeightMap.OCEAN_FLOOR_WG).getType() + " at: " + world.getHighestBlockYAt(0, 0, HeightMap.OCEAN_FLOOR_WG));
        plugin.getLogger().info("Highest World_Surface: " + world.getHighestBlockAt(0, 0, HeightMap.WORLD_SURFACE).getType() + " at: " + world.getHighestBlockYAt(0, 0, HeightMap.WORLD_SURFACE));
        plugin.getLogger().info("Highest World_Surface_World_Gen: " + world.getHighestBlockAt(0, 0, HeightMap.WORLD_SURFACE_WG).getType() + " at: " + world.getHighestBlockYAt(0, 0, HeightMap.WORLD_SURFACE_WG));
        plugin.getLogger().info("Does it have a clealing? " + world.hasCeiling());
        plugin.getLogger().info("It's sea level is: " + world.getSeaLevel());
        plugin.getLogger().info("It's logical height is: " + world.getLogicalHeight());
        plugin.getLogger().info("It's maximum height is: " + world.getMaxHeight());
        plugin.getLogger().info("Does it have skylight access? " + world.hasSkyLight());

        int spawnPlatformY = getSpawnPlatformY(world, useVanillaSpawn);
        printPlatform(world, spawnPlatformY);
        printPortalInside(world, spawnPlatformY+2);
        printPortalFrame(world, spawnPlatformY+1);

        return spawnPlatformY;
    }


    private int getSpawnPlatformY(World world, boolean useVanillaSpawn) {

        //get corner coordinates of the chunk spawn is in
        int spawnX = useVanillaSpawn ? (int)Math.floor(world.getSpawnLocation().getX()/16) : 0;
        int spawnZ = useVanillaSpawn ? (int)Math.floor(world.getSpawnLocation().getZ()/16) : 0;
        int spawnY = 500;

        //get world max and min y levels
        int maxY = world.getHighestBlockYAt(spawnX, spawnZ);
        int minY = world.getMinHeight();

        ChunkSnapshot spawnChunk = world.getChunkAt(spawnX, spawnZ).getChunkSnapshot(true, false, false);
        if (world.hasCeiling()) {

            //check in 3x4 square if location is safe
            for (int y = minY; y < maxY; y++) {
                for (int x = spawnX; x <= spawnX+2; x++) {
                    for (int z= spawnZ; z <= spawnZ+3; z++) {
                        Material block = spawnChunk.getBlockType(x, y, z);
                        if (block.isSolid() || block.equals(Material.WATER) || block.equals(Material.LAVA)) {
                            if (spawnChunk.getBlockType(x, y+1, z).isAir() &&
                                    spawnChunk.getBlockType(x, y+2, z).isAir() &&
                                    spawnChunk.getBlockType(x, y+3, z).isAir() &&
                                    spawnChunk.getBlockType(x, y+4, z).isAir()) {
                                spawnY = y;
                                break;
                            }
                        }
                    }
                }
            }
        }

        else if (world.hasSkyLight()) {
            spawnY = getHighestBlockY(spawnChunk, minY);
        }

        else {
            if (world.getEnvironment().equals(World.Environment.NETHER)) {
                spawnY = 31;
            }

            else if (world.getEnvironment().equals(World.Environment.THE_END)) {
                spawnY = spawnChunk.getHighestBlockYAt(0, 0) + 3;
            }
        }

       return spawnY;
    }

    //check in a 5x6 square around 0,0 what the highest block is
    private int getHighestBlockY(ChunkSnapshot spawnChunk, int worldMinY) {

        int highest = worldMinY;
        for (int x = 0; x <= 6; x++) {
            for (int z = 0; z <= 5; z++) {
                int y = spawnChunk.getHighestBlockYAt(x, z);
                if (y > highest) {
                    highest = y;
                }
            }
        }
        return highest;
    }

    //print an 8x8 platform underneath the portal
    private void printPlatform(World world, int y) {
        for (int x = 0; x <= 6; x++) {
            for (int z = 0; z <= 5; z++) {
                world.setType(x, y, z, Material.QUARTZ_BLOCK);
            }
        }
    }

    //print portal frame with x:0 and z:-1, 0, 1 and 2
    private void printPortalFrame(World world, int startHeight) {
        for (int z = 1; z <= 4; z++) {
            if (z == 1 || z == 4) {
                for (int y = startHeight; y <= startHeight+4; y++) {
                    world.setType(3, y, z, Material.CRYING_OBSIDIAN);
                }
            }

            if (z == 2 || z == 3) {
                world.setType(3, startHeight, z, Material.CRYING_OBSIDIAN);
                world.setType(3, startHeight+4, z, Material.CRYING_OBSIDIAN);
            }
        }
    }

    //print the inside of the portal with x:3 and z:2-3
    private void printPortalInside(World world, int startHeight) {
        world.setType(3, startHeight, 2, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(3, startHeight+1, 2, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(3, startHeight+2, 2, Material.PURPLE_STAINED_GLASS_PANE);

        world.setType(3, startHeight, 3, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(3, startHeight+1, 3, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(3, startHeight+2, 3, Material.PURPLE_STAINED_GLASS_PANE);
    }
}
