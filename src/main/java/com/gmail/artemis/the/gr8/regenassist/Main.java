package com.gmail.artemis.the.gr8.regenassist;

import com.gmail.artemis.the.gr8.regenassist.commands.ConfirmCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.RegenCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.TabCompleter;
import com.gmail.artemis.the.gr8.regenassist.listeners.JoinListener;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.MultiverseHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.Utilities;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    private MVWorldManager worldManager;
    private MultiverseHandler mvHandler;
    private MessageHandler msg;
    private Utilities utils;
    private File regenDataFile;
    private FileConfiguration regenData;


    @Override
    public void onEnable() {
        //get an instance of the MVWorldManager from the Multiverse API
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if(core == null) {
            getLogger().severe("Multiverse-Core not found, RegenAssist cannot live without it </3");
            return;
        }
        worldManager = core.getMVWorldManager();

        //get an instance of the MessageHandler and Utilities class
        msg = new MessageHandler();
        utils = new Utilities();

        //pass the MVWorldManager and MessageHandler on to the MultiverseHandler class
        mvHandler = new MultiverseHandler(worldManager, msg);

        //create a general config file if none exists yet
        //create a file to write world- and player-data to if none exists yet
        this.saveDefaultConfig();
        createRegenDataFile();

        //set command executors and pass the relevant instances on
        this.getCommand("regen").setExecutor(new RegenCommand(msg, utils, this));
        this.getCommand("regen").setTabCompleter(new TabCompleter(utils));
        this.getCommand("regenconfirm").setExecutor(new ConfirmCommand(msg, utils, mvHandler, this));

        //register the JoinListener
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        getLogger().info("Enabled RegenAssist");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled RegenAssist");
    }

    private void createRegenDataFile() {
        regenDataFile = new File(getDataFolder(), "regenData.yml");
        if (!regenDataFile.exists()) {
            regenDataFile.getParentFile().mkdirs();
            try {
                regenDataFile.createNewFile();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        regenData = YamlConfiguration.loadConfiguration(regenDataFile);
    }



    //Attempt to remove players from world
    //Plan 1
        //create method to remove offline Players from World
        //get offline player list
        //get list of offline players with location in resourceworld
        //put them at world spawn and remove them from list (use hashmap?)
    //Use logger to output which players have been relocated
    //Send message in chat ("x, y and z have been relocated to spawn")

    //Plan 2
        //relocate everyone who logs in in a world after it has been reset
        //Create file with timestamp for last reset of specific world
        //on PlayerJoin: check whether last login time is before reset
        //if yes, tp them to spawn and send them a message letting them know what happened

    //Perform regen for specified world (only resourceworlds allowed)
    //set doFireTick to false
    //Send message in chat ("world has been regenerated!")


}
