package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

public class PortalHandler {

    private Main plugin;

    public PortalHandler(Main p) {
        plugin = p;
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
