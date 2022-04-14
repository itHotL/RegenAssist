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

    //this currently supports a single portal --> maybe more in the future?
    public boolean relocatePotentialPortal (CommandSender sender, String worldName) {
        List<MVPortal> portals = portalManager.getAllPortals();

        for (MVPortal portal : portals) {
            if (portal.getLocation().getMVWorld().getName().equalsIgnoreCase(worldName)) {
                sender.sendMessage(MessageWriter.portalFound());
                plugin.getLogger().info("Portal \"" + portal.getName() + "\" found");

                String successMessage = (portal.setPortalLocation(createPortalStructure(worldName), worldName)) ? "Portal moved!" : "Moving portal failed.";
                mvpAPI.savePortalsConfig();
                mvpAPI.reloadConfigs();
                sender.sendMessage(successMessage);
                return true;
            }
        }
        return false;
    }

    //creates a portal structure at spawn and return the location for the mvportal to move to (or empty string if world is null)
    private String createPortalStructure(String worldname) {
        World world = Bukkit.getServer().getWorld(worldname);

        if (world != null) {
            int floorLevel = getHighestBlockAtSpawn(world);

            //print all the different portal parts and get the location for the mvportal to move to
            printPlatform(world, floorLevel);
            String mvportalLocation = printPortalInside(world, floorLevel+2);
            printPortalFrame(world, floorLevel+1);
            return mvportalLocation;
        }
        return "";
    }

    //checks in a 8x8 radius around 0,0 what the highest block is
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

    //print an 8x8 platform underneath the portal and set spawn on this platform
    private void printPlatform(World world, int y) {
        for (int x = -3; x <= 4; x++) {
            for (int z = -3; z <= 4; z++) {
                world.setType(x, y, z, Material.QUARTZ_BLOCK);
            }
        }
        world.setSpawnLocation(3, y+1, 0);
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

    //print the inside of the portal with x:0 and z:0 and 1, and return the location as a string
    private String printPortalInside(World world, int startHeight) {
        world.setType(0, startHeight, 0, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(0, startHeight+1, 0, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(0, startHeight+2, 0, Material.PURPLE_STAINED_GLASS_PANE);

        world.setType(0, startHeight, 1, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(0, startHeight+1, 1, Material.PURPLE_STAINED_GLASS_PANE);
        world.setType(0, startHeight+2, 1, Material.PURPLE_STAINED_GLASS_PANE);
        int endHeight = startHeight+2;
        String mvportalLocation = "0.0,"+startHeight+".0,0.0:0.0,"+endHeight+".0,1.0";
        plugin.getLogger().info("Portal location: " + mvportalLocation);
        return mvportalLocation;
    }
}
