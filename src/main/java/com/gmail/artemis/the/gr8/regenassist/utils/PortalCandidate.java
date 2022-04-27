package com.gmail.artemis.the.gr8.regenassist.utils;

import com.onarandombox.MultiversePortals.MVPortal;
import org.bukkit.Location;
import org.bukkit.World;

public class PortalCandidate {

    private final MVPortal portal;
    private final World world;
    private String location;
    private Location spawnLocation;

    public PortalCandidate(World w, MVPortal p) {
        world = w;
        portal = p;
        spawnLocation = p.getLocation().getMVWorld().getSpawnLocation();
    }

    public void submitNewLocation(String loc, int x, int y, int z) {
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
            return portal.setPortalLocation(location, worldName) && world.setSpawnLocation(spawnLocation);
        }

        else {
            return false;
        }
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public String getName() {
        return portal.getName();
    }
}
