package com.gmail.artemis.the.gr8.regenassist.portal;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import com.onarandombox.MultiversePortals.utils.PortalManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class MyPortalManager {

    private final ConfigHandler config;
    private final Main plugin;

    private final MVPortalsHandler mvp;
    private final PortalPrinter portalPrinter;

    public MyPortalManager(ConfigHandler c, Main p) {
        config = c;
        plugin = p;
        MultiversePortals api = (MultiversePortals) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Portals");

        if (api != null) {
            PortalManager portalManager = api.getPortalManager();
            mvp = new MVPortalsHandler(api, portalManager);
            portalPrinter = new PortalPrinter(c);
        }

        else {
            mvp = null;
            portalPrinter = null;
        }
    }

    public boolean hasMVPortal(String worldName) {
        if (mvp != null) {
            List<MVPortal> portals = mvp.getAllPortals();
            for (MVPortal p : portals) {
                if (p.getLocation().getMVWorld().getName().equalsIgnoreCase(worldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public PortalResult fixPotentialPortal(String worldName) {
        if (mvp != null) {
            MVPortal portal = findMVPortal(worldName);
            if (portal != null) {
                PortalResult printedPortal = getNewPortalStructure(worldName);
                if (printedPortal != null) {
                    String mvportalLocation = printedPortal.getMVPortalStringLocation();
                    plugin.getLogger().info("Saving new portal location to the Multiverse-Portals config and reloading config");
                    if (mvp.setPortalLocation(portal, mvportalLocation, worldName) && mvp.savePortalsConfig()) {
                        mvp.reloadConfigs();
                        printedPortal.setPortalName(portal.getName());
                        return printedPortal;
                    }
                }
            }
        }
        return null;
    }

    private MVPortal findMVPortal(String worldName) {
        List<MVPortal> portals = mvp.getAllPortals();
        for (MVPortal p : portals) {
            if (p.getLocation().getMVWorld().getName().equalsIgnoreCase(worldName)) {
                return p;
            }
        }
        return null;
    }

    private PortalResult getNewPortalStructure(String worldName) {
        World world = Bukkit.getServer().getWorld(worldName);
        if (world != null) {
            Location platformLocation = LocationFinder.findSafePortalLocation(world, config.useVanillaSpawn());
            if (platformLocation == null) {
                plugin.getLogger().warning("Could not find a safe place for a portal structure!");
                return null;
            }
            else {
                PortalResult printedPortal = portalPrinter.printPortal(world, platformLocation);
                if (printedPortal == null) {
                    plugin.getLogger().warning("Could not create a new portal structure!");
                    return null;
                }
                else {
                    return printedPortal;
                }
            }
        }
        return null;
    }
}
