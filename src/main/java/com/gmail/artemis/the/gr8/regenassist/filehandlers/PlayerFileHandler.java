package com.gmail.artemis.the.gr8.regenassist.filehandlers;

import com.gmail.artemis.the.gr8.regenassist.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerFileHandler {

    private File playerFile;
    private FileConfiguration player;
    private final Main plugin;

    public PlayerFileHandler(Main p) {
        plugin = p;
    }


    //load the datafile that will store regen-data (called in onEnable)
    public void loadDataFile() {

        playerFile = new File(plugin.getDataFolder(), "players.yml");
        if (!playerFile.exists()) {
            createDataFile();
        }

        player = new YamlConfiguration();
        try {
            player.load(playerFile);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        player.options().header("This file stores the world that players have logged out in, so RegenAssist can teleport them to safety if this world has been regenerated");
        player.options().copyHeader(true);
        saveDataFile();
    }

    //reload data from file (called in ReloadCommand)
    public boolean reloadDataFile() {
        try {
            player = YamlConfiguration.loadConfiguration(playerFile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //store dates in the regen-data file (param: playername & worldname)
    public void writeToDataFile(String playerName, String worldName) {
        try {
            player.set(playerName, worldName);
            boolean saved = saveDataFile();
            if (saved) {
                plugin.getLogger().info("Successfully saved logout world ["+worldName+"] in player.yml");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDataFile() {
        playerFile.getParentFile().mkdirs();
        try {
            playerFile.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean saveDataFile() {
        try {
            player.save(playerFile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
