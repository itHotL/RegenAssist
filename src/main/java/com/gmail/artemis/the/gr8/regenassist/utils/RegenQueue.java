package com.gmail.artemis.the.gr8.regenassist.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RegenQueue {

    //hashMap with a unique code for worlds that are queued to be regenerated
    //key = UUID, value = object containing worldname, seed-option and gamerules-option
    private HashMap<UUID, RegenCandidate> regenQueue = new HashMap<>();

    //create and return a unique identifying key for the world and store relevant properties in the entry
    public UUID createEntry(String worldName, String seedOption, String resetGameRules) {
        RegenCandidate regenCandidate = new RegenCandidate(worldName, seedOption, resetGameRules);
        UUID uuid = UUID.randomUUID();
        regenQueue.put(uuid, regenCandidate);
        return uuid;
    }

    public boolean containsWorldName(String worldName) {
        if (regenQueue.isEmpty()) {
            return false;
        }

        else {
            Optional<Map.Entry<UUID, RegenCandidate>> matchedEntry =
                regenQueue.entrySet().stream().filter(element ->
                element.getValue().getWorldName().equalsIgnoreCase(worldName)).findAny();
            return matchedEntry.isPresent();
        }
    }

    public boolean containsWorldCode(UUID uuid) {
        return regenQueue.containsKey(uuid);
    }

    public UUID getWorldCode(String worldName) {
        Optional<Map.Entry<UUID, RegenCandidate>> matchedEntry =
            regenQueue.entrySet().stream().filter(element ->
            element.getValue().getWorldName().equalsIgnoreCase(worldName)).findAny();
        return matchedEntry.isPresent() ? matchedEntry.get().getKey() : null;
    }

    //retrieve and remove an entry from the list
    public RegenCandidate removeEntry(UUID uuid) {
        return regenQueue.remove(uuid);
    }
}



