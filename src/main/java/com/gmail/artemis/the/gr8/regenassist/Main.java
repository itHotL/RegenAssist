package com.gmail.artemis.the.gr8.regenassist;

import com.gmail.artemis.the.gr8.regenassist.commands.ConfirmCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.RegenCommand;
import com.gmail.artemis.the.gr8.regenassist.commands.TabCompleter;
import com.gmail.artemis.the.gr8.regenassist.listeners.JoinListener;
import com.gmail.artemis.the.gr8.regenassist.utils.FileHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageWriter;
import com.gmail.artemis.the.gr8.regenassist.utils.MultiverseHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.Utilities;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private MVWorldManager worldManager;
    private MultiverseHandler mvHandler;
    private MessageWriter msg;
    private FileHandler fw;
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

        //get an instance of the MessageWriter, FileHandler and Utilities class
        msg = new MessageWriter();
        utils = new Utilities();
        fw = new FileHandler(this);

        //pass the MVWorldManager and MessageWriter on to the MultiverseHandler class
        mvHandler = new MultiverseHandler(worldManager, msg);

        //create a general config and data storage file if none exist yet, and load them
        this.saveDefaultConfig();
        fw.writeToFile();

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
