package com.gmail.artemis.the.gr8.regenassist.portal;

import org.bukkit.*;

public final class LocationFinder {

    private LocationFinder() {
    }

    //gets the lowest coordinate that is still inside the same chunk
    public static int getChunkCorner(int x) {
        return x-(x%16);
    }

    public static Location findSafePortalLocation(World world, boolean useVanillaSpawn) {
        Location platformLocation;
        if (useVanillaSpawn) {
            int x = LocationFinder.getChunkCorner(world.getSpawnLocation().getBlockX());
            int z = LocationFinder.getChunkCorner(world.getSpawnLocation().getBlockZ());
            int y = LocationFinder.getSafeYLevel(world, x, z);
            platformLocation = new Location(world, x, y, z);
        }

        else {
            platformLocation = new Location(world, 0, LocationFinder.getSafeYLevel(world, 0, 0), 0);
        }

        if (platformLocation.getBlockY()==500) {
            return null;
        }

        return platformLocation;
    }

    //checks what kind of world we are dealing with, and calls appropriate methods to find a safe y-level (returns 500 if none are found)
    private static int getSafeYLevel(World world, int chunkCornerX, int chunkCornerZ) {
        int platformY = 500;
        int maxY = world.getMaxHeight();
        int minY = world.getMinHeight();

        ChunkSnapshot spawnChunk = world.getChunkAt(chunkCornerX, chunkCornerZ).getChunkSnapshot(true, false, false);

        if (world.hasSkyLight()) {
            platformY = getHighestBlockY(spawnChunk, minY, chunkCornerX, chunkCornerZ);
        }

        else if (world.hasCeiling()) {
            platformY = getHighestBlockYNoClealing(spawnChunk, minY, maxY, chunkCornerX, chunkCornerZ);
            if (platformY==500) {
                if (world.getEnvironment().equals(World.Environment.NETHER)) {
                    platformY = 31;
                }
            }
        }

        else if (world.getEnvironment().equals(World.Environment.THE_END)) {
            platformY = spawnChunk.getHighestBlockYAt(world.getSpawnLocation().getBlockX()+16, world.getSpawnLocation().getBlockZ()+16);
        }

        return platformY;
    }

    //check in a 5x6 square around 0,0 what the highest non-leaf, non-log block is
    private static int getHighestBlockY(ChunkSnapshot spawnChunk, int worldMinY, int platformX, int platformZ) {

        int platformY = worldMinY;
        for (int x = platformX; x <= platformX+6; x++) {
            for (int z = platformZ; z <= platformZ+5; z++) {
                int y = spawnChunk.getHighestBlockYAt(x, z);
                if (y > platformY && !Tag.LOGS.isTagged(spawnChunk.getBlockType(x, y, z)) && !Tag.LEAVES.isTagged(spawnChunk.getBlockType(x, y, z))) {
                    platformY = y;
                }
            }
        }
        return platformY;
    }

    //check in 3x4 square if location is safe
    private static int getHighestBlockYNoClealing(ChunkSnapshot spawnChunk, int worldMinY, int worldMaxY, int platformX, int platformZ) {
        for (int y = worldMaxY; y > worldMinY; y--) {
            for (int x = platformX; x <= platformX+2; x++) {
                for (int z= platformZ; z <= platformZ+3; z++) {
                    Material block = spawnChunk.getBlockType(x, y, z);
                    if (block.isSolid() || block.equals(Material.WATER) || block.equals(Material.LAVA)) {
                        if (spawnChunk.getBlockType(x, y+1, z).isAir() &&
                                spawnChunk.getBlockType(x, y+2, z).isAir() &&
                                spawnChunk.getBlockType(x, y+3, z).isAir() &&
                                spawnChunk.getBlockType(x, y+4, z).isAir()) {
                            return y;
                        }
                    }
                }
            }
        }
        return 500;
    }
}
