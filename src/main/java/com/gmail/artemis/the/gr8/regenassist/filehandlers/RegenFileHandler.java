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
    private FileConfiguration regenConf;
    private final Main plugin;

    //when the constructor is called, create a datafile if it does not exist yet (happens in onEnable)
    public RegenFileHandler(Main p) {
        plugin = p;
        loadFile();
    }

    //load the datafile that will store regen-data (called in onEnable)
    public void loadFile() {

        regenFile = new File(plugin.getDataFolder(), "regen.yml");
        if (!regenFile.exists()) {
            createFile();
        }

        regenConf = new YamlConfiguration();
        try {
            regenConf.load(regenFile);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        regenConf.options().header("This file stores the date and time your worlds have last been regenerated");
        regenConf.options().copyHeader(true);
        saveFile();
    }

    //reload data from file (called in ReloadCommand)
    public boolean reloadFile() {
        try {
            if (!regenFile.exists()) {
                loadFile();
            }
            regenConf = YamlConfiguration.loadConfiguration(regenFile);
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
            regenConf.set(worldName, date.toString());
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
        return (regenConf.contains(worldName));
    }

    public String getLastRegenTime(String worldName) {
        return (regenConf.contains(worldName)) ? regenConf.getString(worldName) : "";
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
            regenConf.save(regenFile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

