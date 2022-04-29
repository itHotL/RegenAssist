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
import com.gmail.artemis.the.gr8.regenassist.portal.MyPortalManager;
import com.gmail.artemis.the.gr8.regenassist.regen.MVCoreHandler;
import com.gmail.artemis.the.gr8.regenassist.regen.RegenQueue;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class Main extends JavaPlugin {

    private PlayerFileHandler playerFile;

    @Override
    public void onEnable() {
        //get an instance of all the classes that need to be instantiated
        ConfigHandler config = new ConfigHandler(this);
        playerFile = new PlayerFileHandler(this);
        RegenFileHandler regenFile = new RegenFileHandler(this);
        RegenQueue regenQueue = new RegenQueue(this);

        //get an instance of the MVWorldManager from the Multiverse API and pass it on to the MVCoreHandler
        MultiverseCore core = (MultiverseCore)Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if (core == null) {
            getLogger().severe("Multiverse-Core not found, RegenAssist is nothing without it");
            return;
        }
        MVWorldManager worldManager = core.getMVWorldManager();
        MVCoreHandler mvCoreHandler = new MVCoreHandler(worldManager);

        //instantiate the MyPortalManager and RegenManager
        MyPortalManager myPortalManager = new MyPortalManager(config, this);
        RegenManager regenManager = new RegenManager(config, mvCoreHandler, myPortalManager, regenFile, regenQueue, this);

        //set command executors and pass the relevant instances on
        this.getCommand("regen").setExecutor(new RegenCommand(config, mvCoreHandler, regenManager, regenQueue));
        this.getCommand("regen").setTabCompleter(new TabCompleter(config));
        this.getCommand("regenconfirm").setExecutor(new ConfirmCommand(regenManager, regenQueue));
        this.getCommand("regenreload").setExecutor(new ReloadCommand(config, playerFile, regenFile));

        //register the Listeners
        Bukkit.getPluginManager().registerEvents(new JoinListener(config, playerFile, regenFile, this), this);
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
