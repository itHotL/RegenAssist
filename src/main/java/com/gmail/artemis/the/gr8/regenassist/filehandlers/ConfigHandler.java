package com.gmail.artemis.the.gr8.regenassist.filehandlers;

import com.gmail.artemis.the.gr8.regenassist.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;


public class ConfigHandler {

    private File configFile;
    private FileConfiguration config;
    private final Main plugin;

    public ConfigHandler(Main p) {
        plugin = p;
        saveDefaultConfig();
    }

    public boolean restorePortal() {
        return config.getBoolean("restore portal");
    }

    public String getMainWorldName() {
        return config.getString("main world");
    }

    public String getSpawnWorldName() {
        return config.getString("spawnworld");
    }

    public List<String> getWorldList() {
        return config.getStringList("worlds");
    }

    //reload data from file (called in ReloadCommand)
    public boolean reloadFile() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //create a config file if none exists yet (from the config.yml in the plugin's resources)
    private void saveDefaultConfig() {
        config = plugin.getConfig();
        plugin.saveDefaultConfig();
        configFile = new File(plugin.getDataFolder(), "config.yml");
    }
}
