package com.gmail.artemis.the.gr8.regenassist.utils;

import java.util.HashMap;
import java.util.UUID;

public class RegenQueue {

    //hashMap with a unique code for worlds that are queued to be regenerated
    //key = UUID, value = worldName
    private HashMap<UUID, String> worldCodes = new HashMap<>();

    //create and return a unique identifying key for the world
    public UUID createWorldCode(String worldName) {
        UUID uuid = UUID.randomUUID();
        worldCodes.put(uuid, worldName);
        return uuid;
    }

    public boolean containsWorldName(String worldName) {
        return worldCodes.containsValue(worldName);
    }

    public boolean containsWorldCode(UUID uuid) {
        return worldCodes.containsKey(uuid);
    }

    //retrieve and remove an entry from the list
    public String removeEntry(UUID uuid) {
        return worldCodes.remove(uuid);
    }
}



