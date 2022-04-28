package com.gmail.artemis.the.gr8.regenassist.portal;

import org.bukkit.Location;

public class PortalResult {

    private final Location portalInsideCorner1;
    private final Location portalInsideCorner2;
    private final Location spawnLocation;
    private String portalName;

    public PortalResult(Location portalCorner1, Location portalCorner2, Location playerSpawn) {
        portalInsideCorner1 = portalCorner1;
        portalInsideCorner2 = portalCorner2;
        spawnLocation = playerSpawn;
    }

    public Location getPortalInsideCorner1() {
        return portalInsideCorner1;
    }

    public Location getPortalInsideCorner2() {
        return portalInsideCorner2;
    }

    public String getMVPortalStringLocation() {
        double x1 = portalInsideCorner1.getX();
        double y1 = portalInsideCorner1.getY();
        double z1 = portalInsideCorner1.getZ();
        double x2 = portalInsideCorner2.getX();
        double y2 = portalInsideCorner2.getY();
        double z2 = portalInsideCorner2.getZ();
        return x1 + "," + y1 + "," + z1 + ":" + x2 + "," + y2 + "," + z2;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String name) {
        portalName = name;
    }
}
