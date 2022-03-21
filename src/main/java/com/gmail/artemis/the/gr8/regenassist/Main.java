package com.gmail.artemis.the.gr8.regenassist;

import com.gmail.artemis.the.gr8.regenassist.commands.ConfirmCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.RegenCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.TabCompleter;
import com.gmail.artemis.the.gr8.regenassist.listeners.JoinListener;
import com.gmail.artemis.the.gr8.regenassist.utils.*;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    private MVWorldManager worldManager;
    private MultiverseHandler mv;
    private DataFileHandler data;
    private ConfigHandler conf;
    private Utilities utils;


    @Override
    public void onEnable() {
        //get an instance of the MVWorldManager from the Multiverse API
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if(core == null) {
            getLogger().severe("Multiverse-Core not found, RegenAssist cannot live without it </3");
            return;
        }
        worldManager = core.getMVWorldManager();

        //get an instance of the ConfigHandler, DataFileHandler and Utilities class
        utils = new Utilities();
        data = new DataFileHandler(this);
        conf = new ConfigHandler(this);

        //pass the MVWorldManager and MessageWriter on to the MultiverseHandler class
        mv = new MultiverseHandler(worldManager);

        //create a general config and data storage file if none exist yet, and load them
        conf.saveDefaultConfig();
        data.loadDataFile();

        getLogger().info("Worldlist: "+conf.getWorldList());

        //set command executors and pass the relevant instances on
        this.getCommand("regen").setExecutor(new RegenCommand(utils, conf, data, mv,this));
        this.getCommand("regen").setTabCompleter(new TabCompleter(conf));
        this.getCommand("regenconfirm").setExecutor(new ConfirmCommand(utils, mv, data, this));

        //register the JoinListener
        Bukkit.getPluginManager().registerEvents(new JoinListener(data, this), this);

        getLogger().info("Enabled RegenAssist");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled RegenAssist");
    }

}
