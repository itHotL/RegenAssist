package com.gmail.artemis.the.gr8.regenassist.portal;

import com.onarandombox.MultiversePortals.MVPortal;
import org.bukkit.Location;
import org.bukkit.World;

public class PortalRelocationResult {

    private final MVPortal portal;
    private final World world;
    private String location;
    private Location spawnLocation;

    public PortalRelocationResult(World w, MVPortal p) {
        world = w;
        portal = p;
    }

    public void setNewLocation(String loc, int x, int y, int z) {
        location = loc;
        submitNewSpawn(x, y, z);
    }

    private void submitNewSpawn(int x, int y, int z) {
        if (portal != null) {
            spawnLocation = new Location(world, x, y, z, 0.0F, 0.0F);
        }
    }

    public boolean saveSubmittedLocation(String worldName) {
        if (location != null) {
            return portal.setPortalLocation(location, worldName) && saveSubmittedSpawn();
        }

        else {
            return false;
        }
    }

    private boolean saveSubmittedSpawn() {
        return (spawnLocation != null && world.setSpawnLocation(spawnLocation));
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public String getName() {
        return portal.getName();
    }
}
