package com.gmail.artemis.the.gr8.regenassist.filehandlers;

import com.gmail.artemis.the.gr8.regenassist.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class RegenFileHandler {

    private File regenFile;
    private FileConfiguration regen;
    private final Main plugin;


    public RegenFileHandler(Main p) {
        plugin = p;
    }


    //load the datafile that will store regen-data (called in onEnable)
    public void loadFile() {

        regenFile = new File(plugin.getDataFolder(), "regen.yml");
        if (!regenFile.exists()) {
            createFile();
        }

        regen = new YamlConfiguration();
        try {
            regen.load(regenFile);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        regen.options().header("This file stores the date and time your worlds have last been regenerated");
        regen.options().copyHeader(true);
        saveFile();
    }

    //reload data from file (called in ReloadCommand)
    public boolean reloadFile() {
        try {
            regen = YamlConfiguration.loadConfiguration(regenFile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //store dates in the regen-data file (param: worldname, date to store)
    public void writeToFile(String worldName, Instant date) {
        try {
            regen.set(worldName, date.toString());
            boolean saved = saveFile();
            if (saved) {
                plugin.getLogger().info("Successfully put regen-timestamp for "+worldName+" in regen.yml");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasEntry(String worldName) {
        return (regen.contains(worldName));
    }

    public String getLastRegenTime(String worldName) {
        return (regen.contains(worldName)) ? regen.getString(worldName) : "";
    }

    //create a file to write world- and player-data to if none exists yet
    private void createFile() {
        regenFile.getParentFile().mkdirs();
        try {
            regenFile.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //an extra safe save method
    private boolean saveFile() {
        try {
            regen.save(regenFile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

