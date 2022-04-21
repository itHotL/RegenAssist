package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import com.onarandombox.MultiversePortals.utils.PortalManager;
import org.bukkit.*;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MVPortalsHandler {

    //--> keep in mind that the portalManager might be null
    private final MultiversePortals mvpAPI;
    private final PortalManager portalManager;
    private final Main plugin;

    public MVPortalsHandler(MultiversePortals mvph, PortalManager mvp, Main p) {
        mvpAPI = mvph;
        portalManager = mvp;
        plugin = p;
    }

    //check if there is a pre-existing portal, print a new portal structure, move the MVPortal to it
    //return the y-level to set players to spawn at (or 500 if anything fails)
    public double relocatePotentialPortal (CommandSender sender, String worldName) {
        List<MVPortal> portals = portalManager.getAllPortals();

        for (MVPortal portal : portals) {
            if (portal.getLocation().getMVWorld().getName().equalsIgnoreCase(worldName)) {
                sender.sendMessage(MessageWriter.portalFound());
                plugin.getLogger().info("Portal \"" + portal.getName() + "\" found");

                World world = Bukkit.getServer().getWorld(worldName);
                if (world != null) {
                    int platformHeight = createPortalStructure(world);
                    int portalBottom = platformHeight+2;
                    int portalTop = portalBottom+2;
                    String mvportalLocation = "0.0," + portalBottom + ".0,0.0:0.0," + portalTop + ".0,1.0";
                    portal.setPortalLocation(mvportalLocation, worldName);
                    mvpAPI.savePortalsConfig();
                    mvpAPI.reloadConfigs();
                    return platformHeight+1;
                }
            }
        }
        return 500;
    }

    //print all the different portal parts at spawn, and return the y level the platform has been printed on
    private int createPortalStructure(World world) {
        int platformHeight = getHighestBlockAtSpawn(world);
        printPlatform(world, platformHeight);
        printPortalInside(world, platformHeight+2);
        printPortalFrame(world, platformHeight+1);
        return platformHeight;
    }

    //check in a 8x8 radius around 0,0 what the highest block is
    private int getHighestBlockAtSpawn(World world) {
        int highest = 0;
        for (int x = -3; x <= 4; x++) {
            for (int z = -3; z <= 4; z++) {
                int y = world.getHighestBlockYAt(x, z);
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
                for (int y = startHeight; y < startHeight+5; y++) {
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
