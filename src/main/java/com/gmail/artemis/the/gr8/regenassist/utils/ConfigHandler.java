package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;

import java.util.List;


public class ConfigHandler {

    private final Main plugin;

    public ConfigHandler(Main p) {
        plugin = p;
    }

    public void saveDefaultConfig() {
        plugin.saveDefaultConfig();
    }

    public List<String> getWorldList() {
        return plugin.getConfig().getStringList("worlds");
    }
}
