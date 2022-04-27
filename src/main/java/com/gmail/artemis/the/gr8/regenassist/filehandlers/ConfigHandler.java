package com.gmail.artemis.the.gr8.regenassist.filehandlers;

import com.gmail.artemis.the.gr8.regenassist.Main;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
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

    public List<String> getWorldList() {
        return config.getStringList("worlds");
    }

    public String getSpawnWorldName() {
        return config.getString("spawnworld");
    }

    public boolean restorePortal() {
        return config.getBoolean("restore portal");
    }

    public boolean useVanillaSpawn() {
        return config.getBoolean("use vanilla spawn");
    }

    public String getPortalPlatformBlock(World.Environment environment) {
        ConfigurationSection platformBlock = config.getConfigurationSection("platform");
        return (platformBlock != null) ? platformBlock.getString(environment.name().toLowerCase()) : "";
    }

    public String getPortalFrameBlock(World.Environment environment) {
        ConfigurationSection frameBlock = config.getConfigurationSection("portal frame");
        return (frameBlock != null) ? frameBlock.getString(environment.name().toLowerCase()) : "";
    }

    public String getPortalInsideBlock(World.Environment environment) {
        ConfigurationSection insideBlock = config.getConfigurationSection("portal inside");
        return (insideBlock != null) ? insideBlock.getString(environment.name().toLowerCase()) : "";
    }

    public String getMainWorldName() {
        return config.getString("main world");
    }


    //reload data from file (called in ReloadCommand)
    public boolean reloadFile() {
        try {
            if (!configFile.exists()) {
                saveDefaultConfig();
            }
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
