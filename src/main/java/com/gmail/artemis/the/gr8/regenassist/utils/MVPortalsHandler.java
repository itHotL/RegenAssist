package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.utils.PortalManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;

public class MVPortalsHandler {

    //--> keep in mind that the portalManager might be null
    private final PortalManager portalManager;
    private final Main plugin;

    public MVPortalsHandler(PortalManager mvp, Main p) {
        portalManager = mvp;
        plugin = p;
    }

    public void findOldPortals(String worldName) {
        List<MVPortal> portals = portalManager.getAllPortals();
        HashMap<MVPortal, String> portalsFromWorld = new HashMap<>();
        HashMap<MVPortal, String> portalsToWorld = new HashMap<>();

        for (MVPortal portal : portals) {
            if (portal.getLocation().getMVWorld().getName().equalsIgnoreCase(worldName)) {
                portalsFromWorld.put(portal, portal.getName());
                plugin.getLogger().info("Relevant portal \"" + portal.getName() + "\" found");
            }

            if (portal.getDestination().toString().equalsIgnoreCase(worldName)) {
                portalsToWorld.put(portal, portal.getName());
                plugin.getLogger().info("Relevant portal \"" + portal.getName() + "\" found");
            }
        }
    }

    public void getSafePortalLocation(String worldname) {
        if (Bukkit.getServer().getWorld(worldname) != null) {
            World world = Bukkit.getServer().getWorld(worldname);

            int y0 = world.getHighestBlockYAt(0, 0);
            int y1 = world.getHighestBlockYAt(0, 1);
            int y2 = world.getHighestBlockYAt(0, 2);
            int y3 = world.getHighestBlockYAt(0, 3);

            plugin.getLogger().info("Highest coords in a row: " +y0 + ", " + y1 + ", " + y2 + ", " + y3);

            int high1 = Math.max(y0, y1);
            int high2 = Math.max(y2, y3);
            int highest = Math.max(high1, high2);
            world.setType(0, highest, 0, Material.CRYING_OBSIDIAN);
            world.setType(0, highest, 1, Material.CRYING_OBSIDIAN);
            world.setType(0, highest, 2, Material.CRYING_OBSIDIAN);
            world.setType(0, highest, 3, Material.CRYING_OBSIDIAN);

            plugin.getLogger().info("Printed crying obsidian at y-level " + highest);
        }
    }
}
