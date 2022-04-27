package com.gmail.artemis.the.gr8.regenassist.filehandlers;

import com.gmail.artemis.the.gr8.regenassist.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PlayerFileHandler {

    private File playerFile;
    private FileConfiguration playerConf;
    private final Main plugin;

    //when the constructor is called, create a datafile if it does not exist yet (happens in onEnable)
    public PlayerFileHandler(Main p) {
        plugin = p;
        loadFile();
    }


    //load the datafile that will store regen-data (called in onEnable)
    public void loadFile() {

        playerFile = new File(plugin.getDataFolder(), "players.yml");
        if (!playerFile.exists()) {
            createFile();
        }

        playerConf = new YamlConfiguration();
        try {
            playerConf.load(playerFile);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        playerConf.options().header("This file stores the world that players have logged out in, so RegenAssist can teleport them to safety if this world has been regenerated");
        playerConf.options().copyHeader(true);
        saveFile();
    }

    //reload data from file (called in ReloadCommand)
    public boolean reloadFile() {
        try {
            if (!playerFile.exists()) {
                loadFile();
            }
            playerConf = YamlConfiguration.loadConfiguration(playerFile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //store dates in the regen-data file (param: playername & worldname)
    public void writeToFile(String playerName, String worldName) {
        try {
            playerConf.set(playerName, worldName);
            boolean saved = saveFile();
            if (saved) {
                plugin.getLogger().info("Successfully saved logout world ["+worldName+"] in player.yml");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasEntry(String playerName) {
        return (playerConf.contains(playerName));
    }

    public String getLogoutWorld(String playerName) {
        return (playerConf.contains(playerName)) ? playerConf.getString(playerName) : "";
    }

    private void createFile() {
        playerFile.getParentFile().mkdirs();
        try {
            playerFile.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean saveFile() {
        try {
            playerConf.save(playerFile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
