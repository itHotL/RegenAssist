package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class DataFileHandler {

    private File dataFile;
    private FileConfiguration data;
    private final Main plugin;


    public DataFileHandler(Main p) {
        plugin = p;
    }

    public String getLastRegenTime(String worldName) {
        String lastRegenTime;

        try {
            lastRegenTime = (data.contains(worldName)) ? data.getString(worldName) : "";
        }
        catch (Exception e){
            lastRegenTime = "";
            e.printStackTrace();
        }

        return lastRegenTime;
    }

    //store values in the regen-data file (param: path, object to store)
    public void writeToDataFile(String worldName, LocalDateTime date) {
        try {
            data.set(worldName, date.toString());
            saveDataFile();
            plugin.getLogger().info("Successfully put regen-timestamp for "+worldName+" in data.yml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load the datafile that will store regen-data (called in onEnable)
    public void loadDataFile() {

        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            createDataFile();
        }

        data = new YamlConfiguration();
        try {
            data.load(dataFile);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        data.options().header("This file stores the date and time your worlds have last been regenerated");
        data.options().copyHeader(true);
        saveDataFile();
    }

    //create a file to write world- and player-data to if none exists yet
    private void createDataFile() {
        dataFile.getParentFile().mkdirs();
        try {
            dataFile.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //an extra safe save method
    private void saveDataFile() {
        try {
            data.save(dataFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

