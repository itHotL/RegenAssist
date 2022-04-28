package com.gmail.artemis.the.gr8.regenassist.portal;

import org.bukkit.Location;

public class PortalResult {

    public final Location portalInsideCorner1;
    public final Location portalInsideCorner2;
    public final Location spawnLocation;

    public PortalResult(Location portalCorner1, Location portalCorner2, Location playerSpawn) {
        portalInsideCorner1 = portalCorner1;
        portalInsideCorner2 = portalCorner2;
        spawnLocation = playerSpawn;
    }
}
