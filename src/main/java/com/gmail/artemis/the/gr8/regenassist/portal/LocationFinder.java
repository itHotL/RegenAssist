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
        if (world.getEnvironment().equals(World.Environment.THE_END)) {
            platformLocation = new Location(world, 16, LocationFinder.getSafeYLevel(world, 1, 1), 16);
        }

        else if (useVanillaSpawn) {
            int chunkCornerX = LocationFinder.getChunkCorner(world.getSpawnLocation().getBlockX());
            int chunkCornerZ = LocationFinder.getChunkCorner(world.getSpawnLocation().getBlockZ());
            int y = LocationFinder.getSafeYLevel(world, chunkCornerX>>4, chunkCornerZ>>4);
            platformLocation = new Location(world, chunkCornerX, y, chunkCornerZ);
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
    private static int getSafeYLevel(World world, int chunkX, int chunkZ) {
        int platformY = 500;
        int maxY = world.getLogicalHeight()-1;
        int minY = world.getMinHeight()+1;

        ChunkSnapshot spawnChunk = world.getChunkAt(chunkX, chunkZ).getChunkSnapshot(true, false, false);

        if (world.hasSkyLight()) {
            platformY = getHighestBlockY(spawnChunk, minY);
        }

        else if (world.hasCeiling()) {
            platformY = getHighestBlockYNotOnClealing(spawnChunk, minY, maxY);
            if (platformY==500) {
                if (world.getEnvironment().equals(World.Environment.NETHER)) {
                    platformY = 31;
                }
            }
        }

        else if (world.getEnvironment().equals(World.Environment.THE_END)) {
            platformY = getHighestBlockYEnd(spawnChunk, minY, maxY);
            if (platformY==500) {
                platformY = (maxY+minY)/2;
            }
        }

        return platformY;
    }

    //check in a 5x6 square around 0,0 what the highest non-leaf, non-log block is (and check again for regular highest block if none meet the criteria)
    private static int getHighestBlockY(ChunkSnapshot spawnChunk, int worldMinY) {

        int platformY = worldMinY;
        for (int x = 0; x <= 5; x++) {
            for (int z = 0; z <= 6; z++) {
                int y = spawnChunk.getHighestBlockYAt(x, z);
                Material highestBlock = spawnChunk.getBlockType(x, y, z);
                if (!Tag.LEAVES.isTagged(highestBlock) && !Tag.LOGS.isTagged(highestBlock)) {
                    if (y > platformY) {
                        platformY = y;
                    }
                }
            }
        }
        if (platformY == worldMinY) {
            for (int x = 0; x <= 5; x++) {
                for (int z = 0; z <= 6; z++) {
                    int y = spawnChunk.getHighestBlockYAt(x, z);
                    if (y > platformY) {
                        platformY = y;
                    }
                }
            }
        }
        return platformY;
    }

    //check in 5x6 square if location is safe
    private static int getHighestBlockYNotOnClealing(ChunkSnapshot spawnChunk, int worldMinY, int worldMaxY) {
        for (int y = worldMaxY; y > worldMinY; y--) {
            for (int x = 0; x <= 5; x++) {
                for (int z = 0; z <= 6; z++) {
                    Material block = spawnChunk.getBlockType(x, y, z);
                    if ((block.isSolid() || block.equals(Material.LAVA) || block.equals(Material.WATER)) && !block.equals(Material.BEDROCK)) {
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

    private static int getHighestBlockYEnd(ChunkSnapshot endSpawnChunk, int worldMinY, int worldMaxY) {
        for (int y = worldMaxY; y > worldMinY; y--) {
            for (int x = 0; x <=5; x++) {
                for (int z = 0; z <=6; z++) {
                    if (endSpawnChunk.getBlockType(x, y, z).isSolid()) {
                        if (endSpawnChunk.getBlockType(x, y+1, z).isAir() &&
                                endSpawnChunk.getBlockType(x, y+2, z).isAir() &&
                                endSpawnChunk.getBlockType(x, y+3, z).isAir() &&
                                endSpawnChunk.getBlockType(x, y+4, z).isAir()) {
                            return y;
                        }
                    }
                }
            }
        }
        return 500;
    }
}
