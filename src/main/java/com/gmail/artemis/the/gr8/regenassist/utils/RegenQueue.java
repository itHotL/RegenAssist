package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RegenQueue {

    //stores a unique code for worlds that are queued to be regenerated
    //key = UUID, value = object containing worldname, seed-option and gamerules-option
    private HashMap<UUID, RegenCandidate> regenQueue = new HashMap<>();
    private final Main plugin;

    public RegenQueue(Main p) {
        plugin = p;
    }

    //create and return a unique identifying key for the world and store relevant properties in the entry for 15 seconds
    public UUID createEntry(String worldName, String seedOption, String resetGameRules) {
        RegenCandidate regenCandidate = new RegenCandidate(worldName, seedOption, resetGameRules);
        UUID uniqueCode = UUID.randomUUID();

        regenQueue.put(uniqueCode, regenCandidate);
        startTimer(uniqueCode);
        return uniqueCode;
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

    //start 15-second timer that removes unique code from the HashMap if it is still there
    private void startTimer(UUID uuid) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if(containsWorldCode(uuid)) {
                    RegenCandidate canceledCandidate = removeEntry(uuid);
                    plugin.getLogger().info("Removed " + canceledCandidate.getWorldName() + " from the regen queue");
                }
            }
        }, 300L);
    }
}



