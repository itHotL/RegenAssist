package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import com.onarandombox.MultiversePortals.utils.PortalManager;
import org.bukkit.*;

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
    public int relocateFoundPortal(String worldName) {
        if (portalManager != null && portal != null) {
            World world = Bukkit.getServer().getWorld(worldName);
            if (world != null) {
                int spawnPlatformY = createPortalStructure(world);
                int portalBottom = spawnPlatformY+2;
                int portalTop = portalBottom+2;
                String mvportalLocation = "0.0," + portalBottom + ".0,0.0:0.0," + portalTop + ".0,1.0";
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
    private int createPortalStructure(World world) {
        Location zero = new Location(world, 0, 0, 0);
        plugin.getLogger().info("Distance from spawn to 0, 0, 0: " + world.getSpawnLocation().distance(zero));
        plugin.getLogger().info("This is " + world.getSpawnLocation().distance(zero)/16 + " chunks away");

        int spawnPlatformY;
        if (world.hasCeiling()) {
            plugin.getLogger().info("World " + world.getName() + " has a ceiling");
            plugin.getLogger().info("It's sea level is: " + world.getSeaLevel());
            plugin.getLogger().info("It's logical height is: " + world.getLogicalHeight());
            plugin.getLogger().info("It's maximum height is: " + world.getMaxHeight());
            plugin.getLogger().info("Does it have skylight access? " + world.hasSkyLight());

            spawnPlatformY = world.getSpawnLocation().getBlockY()+1;

        }
        else {
            if (world.hasSkyLight()) {
                plugin.getLogger().info("World " + world.getName() + " has skylight access");
            }
            spawnPlatformY = getHighestBlockAtSpawn(world);
            printPlatform(world, spawnPlatformY);
            printPortalInside(world, spawnPlatformY+2);
            printPortalFrame(world, spawnPlatformY+1);
        }
        return spawnPlatformY;
    }

    private int getSafeSpawnLocation(World world) {
        ChunkSnapshot spawnChunk = world.getChunkAt(0, 0).getChunkSnapshot();

        int highest = world.getLogicalHeight();
        int y = world.getMinHeight();
        while (y < highest) {
            if (spawnChunk.getBlockType(0, y, 0).isSolid()) {
                for (int i = y; i <= y+4; i++) {
                    if (!spawnChunk.getBlockType(0, i, 0).isAir()) {
                        //do something
                    }
                }
                if (spawnChunk.getBlockType(0, y+1, 0).isAir() && spawnChunk.getBlockType(0, y+2, 0).isAir()) {

                }
            }
            y=+5;
        }

        return 0;
    }

    //check in a 5x6 square around 0,0 what the highest block is
    private int getHighestBlockAtSpawn(World world) {
        ChunkSnapshot spawnChunk = world.getChunkAt(0, 0).getChunkSnapshot();
        int highest = world.getMinHeight();
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
        for (int x = -3; x <= 3; x++) {
            for (int z = -2; z <= 3; z++) {
                world.setType(x, y, z, Material.QUARTZ_BLOCK);
            }
        }
    }

    //print portal frame with x:0 and z:-1, 0, 1 and 2
    private void printPortalFrame(World world, int startHeight) {
        for (int z = -1; z <= 2; z++) {
            if (z == -1 || z == 2) {
                for (int y = startHeight; y <= startHeight+4; y++) {
                    world.setType(0, y, z, Material.CRYING_OBSIDIAN);
                }
            }

            if (z == 0 || z == 1) {
                world.setType(0, startHeight, z, Material.CRYING_OBSIDIAN);
                world.setType(0, startHeight+4, z, Material.CRYING_OBSIDIAN);
            }
        }
    }

    //print the inside of the portal with x:0 and z:0-1
    private void printPortalInside(World world, int startHeight) {
        world.setType(0, startHeight, 0, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(0, startHeight+1, 0, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(0, startHeight+2, 0, Material.PURPLE_STAINED_GLASS_PANE);

        world.setType(0, startHeight, 1, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(0, startHeight+1, 1, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(0, startHeight+2, 1, Material.PURPLE_STAINED_GLASS_PANE);
    }
}
