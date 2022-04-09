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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;


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
        regenQueue = new RegenQueue(this);

        //create datafiles if none exist yet, and load them
        config.saveDefaultConfig();
        playerFile.loadFile();
        regenFile.loadFile();

        //set command executors and pass the relevant instances on
        this.getCommand("regen").setExecutor(new RegenCommand(config, mv, regenFile, regenQueue));
        this.getCommand("regen").setTabCompleter(new TabCompleter(config));
        this.getCommand("regenconfirm").setExecutor(new ConfirmCommand(mv, regenFile, regenQueue, this));
        this.getCommand("regenreload").setExecutor(new ReloadCommand(config, playerFile, regenFile));

        //register the Listeners
        Bukkit.getPluginManager().registerEvents(new JoinListener(config, mv, playerFile, regenFile, this), this);
        Bukkit.getPluginManager().registerEvents(new QuitListener(playerFile), this);

        getLogger().info("Enabled RegenAssist");
    }

    @Override
    public void onDisable() {
        //get the players that haven't logged out before the server shut down and write their location to the playerFile
        Collection<? extends Player> playerList = Bukkit.getServer().getOnlinePlayers();
        playerList.forEach(player -> playerFile.writeToFile(player.getName(),player.getWorld().getName()));

        getLogger().info("Disabled RegenAssist");
    }
}
