package com.gmail.artemis.the.gr8.regenassist;

import com.gmail.artemis.the.gr8.regenassist.commands.ConfirmCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.RegenCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.ReloadCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.TabCompleter;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.PlayerFileHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.listeners.JoinListener;
import com.gmail.artemis.the.gr8.regenassist.utils.*;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    private MVWorldManager worldManager;
    private MultiverseHandler mv;
    private ConfigHandler conf;
    private PlayerFileHandler plFile;
    private RegenFileHandler regFile;
    private Utilities utils;


    @Override
    public void onEnable() {
        //get an instance of the MVWorldManager from the Multiverse API and pass it on to the MultiverseHandler
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if(core == null) {
            getLogger().severe("Multiverse-Core not found, RegenAssist cannot live without it </3");
            return;
        }
        worldManager = core.getMVWorldManager();
        mv = new MultiverseHandler(worldManager);

        //get an instance of the FileHandlers and Utilities class
        conf = new ConfigHandler(this);
        plFile = new PlayerFileHandler(this);
        regFile = new RegenFileHandler(this);
        utils = new Utilities();

        //create datafiles if none exist yet, and load them
        conf.saveDefaultConfig();
        plFile.loadDataFile();
        regFile.loadDataFile();

        //set command executors and pass the relevant instances on
        this.getCommand("regen").setExecutor(new RegenCommand(conf, mv, regFile, utils,this));
        this.getCommand("regen").setTabCompleter(new TabCompleter(conf));
        this.getCommand("regenconfirm").setExecutor(new ConfirmCommand(mv, regFile, utils, this));
        this.getCommand("regenreload").setExecutor(new ReloadCommand(conf, plFile, regFile));

        //register the JoinListener
        Bukkit.getPluginManager().registerEvents(new JoinListener(regFile, this), this);

        getLogger().info("Enabled RegenAssist");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled RegenAssist");
    }

}
