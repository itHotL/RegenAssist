package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import com.onarandombox.MultiversePortals.utils.PortalManager;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.List;

public class MVPortalsHandler {

    //--> keep in mind that the portalManager might be null
    private final ConfigHandler config;
    private final MultiversePortals mvpAPI;
    private final PortalManager portalManager;
    private final Main plugin;

    private PortalCandidate portal;
    //private MVPortal portal;

    public MVPortalsHandler(ConfigHandler c, MultiversePortals mvp, PortalManager pm, Main p) {
        config = c;
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
                    portal = new PortalCandidate(Bukkit.getWorld(worldName), p);
                    return true;
                }
            }
        }
        return false;
    }

    //if a to-be-relocated portal has been found, this will return the name of said portal
    public String getFoundPortalName() {
        return portal != null ? portal.getName() : null;
    }

    //print a new portal structure, and move the earlier found MVPortal to it
    public boolean relocateFoundPortal(String worldName) {
        if (portalManager != null && portal != null) {
            World world = Bukkit.getServer().getWorld(worldName);
            if (world != null) {
                if (!createPortalStructure(world, config.useVanillaSpawn())) {
                    plugin.getLogger().warning("Could not create a new portal structure");
                    return false;
                }

                else if (!portal.saveSubmittedLocation(worldName)) {
                    plugin.getLogger().warning("Could not save the new location for the found Multiverse Portal, portal was not moved");
                    return false;
                }

                plugin.getLogger().info("Saving new portal location to the Multiverse-Portals config and reloading config");
                mvpAPI.savePortalsConfig();
                mvpAPI.reloadConfigs();
                return true;
            }
        }
        return false;
    }

    //calls all the other private methods in order to check for a safe portal location, and if found, print a portal structure
    //returns the y level the platform has been printed on, or 500 if failed
    private boolean createPortalStructure(World world, boolean useVanillaSpawn) {
        Location zero = new Location(world, 0, 0, 0);
        plugin.getLogger().info("Distance from spawn to 0, 0, 0: " + world.getSpawnLocation().distance(zero));
        plugin.getLogger().info("This is " + world.getSpawnLocation().distance(zero)/16 + " chunks away");
        plugin.getLogger().info("Highest Motion_Blocking_No_Leaves: " + world.getHighestBlockAt(0, 0, HeightMap.MOTION_BLOCKING_NO_LEAVES).getType() + " at: " + world.getHighestBlockYAt(0, 0, HeightMap.MOTION_BLOCKING_NO_LEAVES));

        //get corner coordinates of the chunk to create the portal in
        int platformX = useVanillaSpawn ? (int)Math.floor(world.getSpawnLocation().getX()/16) : 0;
        int platformZ = useVanillaSpawn ? (int)Math.floor(world.getSpawnLocation().getZ()/16) : 0;
        int platformY = getPlatformY(world, platformX, platformZ);

        if (platformY==500) {
            plugin.getLogger().warning("Could not find a safe place for a portal structure!");
            return false;
        }

        else {
            printPlatform(world,platformX, platformY, platformZ);
            printPortalInside(world, platformX, platformY, platformZ);
            printPortalFrame(world, platformX, platformY, platformZ);
            return true;
        }
    }

    //checks what kind of world we are dealing with, and calls appropriate methods to find a safe y-level (returns 500 if none are found)
    private int getPlatformY(World world, int platformX, int platformZ) {
        int platformY = 500;
        int maxY = world.getHighestBlockYAt(platformX, platformZ);
        int minY = world.getMinHeight();

        ChunkSnapshot spawnChunk = world.getChunkAt(platformX, platformZ).getChunkSnapshot(true, false, false);

        if (world.hasSkyLight()) {
            platformY = getHighestBlockY(spawnChunk, minY, platformX, platformZ);
        }

        else if (world.hasCeiling()) {
            platformY = getHighestBlockYNoClealing(spawnChunk, minY, maxY, platformX, platformZ);
            if (platformY==500) {
                if (world.getEnvironment().equals(World.Environment.NETHER)) {
                    platformY = 31;
                }
            }
        }

        else if (world.getEnvironment().equals(World.Environment.THE_END)) {
            platformY = spawnChunk.getHighestBlockYAt(world.getSpawnLocation().getBlockX(), world.getSpawnLocation().getBlockZ());
        }

       return platformY;
    }

    //check in a 5x6 square around 0,0 what the highest block is
    private int getHighestBlockY(ChunkSnapshot spawnChunk, int worldMinY, int platformX, int platformZ) {

        int platformY = worldMinY;
        for (int x = platformX; x <= platformX+6; x++) {
            for (int z = platformZ; z <= platformZ+5; z++) {
                int y = spawnChunk.getHighestBlockYAt(x, z);
                if (y > platformY) {
                    platformY = y;
                }
            }
        }
        return platformY;
    }

    //check in 3x4 square if location is safe
    private int getHighestBlockYNoClealing(ChunkSnapshot spawnChunk, int worldMinY, int worldMaxY, int platformX, int platformZ) {
        int platformY = 500;
        for (int y = worldMinY; y < worldMaxY; y++) {
            for (int x = platformX; x <= platformX+2; x++) {
                for (int z= platformZ; z <= platformZ+3; z++) {
                    Material block = spawnChunk.getBlockType(x, y, z);
                    if (block.isSolid() || block.equals(Material.WATER) || block.equals(Material.LAVA)) {
                        if (spawnChunk.getBlockType(x, y+1, z).isAir() &&
                                spawnChunk.getBlockType(x, y+2, z).isAir() &&
                                spawnChunk.getBlockType(x, y+3, z).isAir() &&
                                spawnChunk.getBlockType(x, y+4, z).isAir()) {
                            platformY = y;
                            break;
                        }
                    }
                }
            }
        }

        return platformY;
    }

    //print an 8x8 platform underneath the portal
    private void printPlatform(World world, int platformX, int platformY, int platformZ) {
        String material = config.getPortalPlatformBlock(world.getEnvironment());
        Material block = Material.getMaterial(material.toUpperCase());
        if (block==null) {
            block = Material.WHITE_WOOL;
        }

        for (int x = platformX; x <= platformX+6; x++) {
            for (int z = platformZ; z <= platformZ+5; z++) {
                world.setType(x, platformY, z, block);
            }
        }
    }

    //print portal frame at set place on platform (1 right, 3 north, 1 up from left bottom corner)
    private void printPortalFrame(World world, int platformX, int platformY, int platformZ) {
        String material = config.getPortalFrameBlock(world.getEnvironment());
        Material block = Material.getMaterial(material.toUpperCase());
        if (block==null) {
            block = Material.WHITE_CONCRETE;
        }

        int frameX = platformX+3;
        int frameZ = platformZ+1;
        int frameY = platformY+1;

        for (int z = frameZ; z <= frameZ+3; z++) {
            if (z == frameZ || z == frameZ+3) {
                for (int y = frameY; y <= frameY+4; y++) {
                    world.setType(frameX, y, z, block);
                }
            }

            if (z == frameZ+1 || z == frameZ+2) {
                world.setType(frameX, frameY, z, block);
                world.setType(frameX, frameY+4, z, block);
            }
        }
    }

    //print the inside of the portal at set place on platform (2 right, 3 north, 2 up from left bottom corner)
    //give the location of the portal-inside to the portal-object we are working with
    private void printPortalInside(World world, int platformX, int platformY, int platformZ) {
        String material = config.getPortalInsideBlock(world.getEnvironment());
        Material block = Material.getMaterial(material.toUpperCase());
        if (block==null) {
            block = Material.WHITE_STAINED_GLASS_PANE;
        }

        int portalX = platformX+3;
        int portalY = platformY+2;
        int portalZ = platformZ+2;

        world.setType(portalX, portalY, portalZ, block);
        world.setType(portalX, portalY+1, portalZ, block);
        world.setType(portalX, portalY+2, portalZ, block);

        world.setType(portalX, portalY, portalZ+1, block);
        world.setType(portalX, portalY+1, portalZ+1, block);
        world.setType(portalX, portalY+2, portalZ+1, block);

        StringBuilder mvportalLocation = new StringBuilder();
        mvportalLocation.append((double)portalX).append(",")
                .append((double)portalY).append(",")
                .append((double)portalZ).append(":")
                .append((double)portalX).append(",")
                .append((double)portalY+2).append(",")
                .append((double)portalZ+1);
        portal.submitNewLocation(mvportalLocation.toString(), platformX+1, platformY+1, platformZ+2);
    }
}
