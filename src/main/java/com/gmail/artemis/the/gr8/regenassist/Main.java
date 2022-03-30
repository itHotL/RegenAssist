package com.gmail.artemis.the.gr8.regenassist;

import com.gmail.artemis.the.gr8.regenassist.commands.ConfirmCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.RegenCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.ReloadCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.TabCompleter;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.PlayerFileHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.listeners.JoinListener;
import com.gmail.artemis.the.gr8.regenassist.listeners.QuitListener;
import com.gmail.artemis.the.gr8.regenassist.utils.*;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

    private MVWorldManager worldManager;
    private MultiverseHandler mv;
    private ConfigHandler config;
    private PlayerFileHandler playerFile;
    private RegenFileHandler regenFile;
    private RegenQueue regenQueue;


    @Override
    public void onEnable() {
        //get an instance of the MVWorldManager from the Multiverse API and pass it on to the MultiverseHandler
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if(core == null) {
            getLogger().severe("Multiverse-Core not found, RegenAssist cannot live without it");
            return;
        }
        worldManager = core.getMVWorldManager();
        mv = new MultiverseHandler(worldManager);

        //get an instance of the FileHandlers and RegenQueue class
        config = new ConfigHandler(this);
        playerFile = new PlayerFileHandler(this);
        regenFile = new RegenFileHandler(this);
        regenQueue = new RegenQueue();

        //create datafiles if none exist yet, and load them
        config.saveDefaultConfig();
        playerFile.loadFile();
        regenFile.loadFile();

        //set command executors and pass the relevant instances on
        this.getCommand("regen").setExecutor(new RegenCommand(config, mv, regenFile, regenQueue,this));
        this.getCommand("regen").setTabCompleter(new TabCompleter(config));
        this.getCommand("regenconfirm").setExecutor(new ConfirmCommand(mv, regenFile, regenQueue, this));
        this.getCommand("regenreload").setExecutor(new ReloadCommand(config, playerFile, regenFile));

        //register the Listeners
        Bukkit.getPluginManager().registerEvents(new JoinListener(playerFile, regenFile, this), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(playerFile), this);

        getLogger().info("Enabled RegenAssist");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled RegenAssist");
    }

}
