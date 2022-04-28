package com.gmail.artemis.the.gr8.regenassist.portal;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import org.bukkit.*;

public class PortalPrinter {

    private final ConfigHandler config;
    private final Main plugin;

    public PortalPrinter(ConfigHandler c, Main p) {
        config = c;
        plugin = p;
    }

    public PortalResult printPortal(World world, Location platformLocation) {

        int platformX = platformLocation.getBlockX();
        int platformY = platformLocation.getBlockY();
        int platformZ = platformLocation.getBlockZ();
        printPlatform(world, platformX, platformY, platformZ);

        int portalX = platformX+3;
        int portalY = platformY+2;
        int portalZ = platformZ+2;
        printPortalInside(world, portalX, portalY, portalZ);

        int frameX = platformX+3;
        int frameZ = platformZ+1;
        int frameY = platformY+1;
        printPortalFrame(world, frameX, frameY, frameZ);

        Location portalCorner1 = new Location(world, portalX, portalY, portalZ);
        Location portalCorner2 = new Location(world, portalX, portalY+2, portalZ+1);
        Location spawnLocation = new Location(world, platformX+1, platformY+1, platformZ+2.5);
        return new PortalResult(portalCorner1, portalCorner2, spawnLocation);
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
                for (int y = platformY; y <= platformY+3; y++) {
                    if (y == platformY) {
                        world.setType(x, y, z, block);
                    }
                    else {
                        world.setType(x, y, z, Material.AIR);
                    }
                }
            }
        }
    }

    //print portal frame at set place on platform
    private void printPortalFrame(World world, int frameX, int frameY, int frameZ) {
        String material = config.getPortalFrameBlock(world.getEnvironment());
        Material block = Material.getMaterial(material.toUpperCase());
        if (block==null) {
            block = Material.WHITE_CONCRETE;
        }

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

    //print the inside of the portal at set place on platform
    private void printPortalInside(World world, int portalX, int portalY, int portalZ) {
        String material = config.getPortalInsideBlock(world.getEnvironment());
        Material block = Material.getMaterial(material.toUpperCase());
        if (block==null) {
            block = Material.WHITE_STAINED_GLASS_PANE;
        }

        world.setType(portalX, portalY, portalZ, block);
        world.setType(portalX, portalY+1, portalZ, block);
        world.setType(portalX, portalY+2, portalZ, block);

        world.setType(portalX, portalY, portalZ+1, block);
        world.setType(portalX, portalY+1, portalZ+1, block);
        world.setType(portalX, portalY+2, portalZ+1, block);
    }
}

