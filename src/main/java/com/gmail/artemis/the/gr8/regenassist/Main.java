package com.gmail.artemis.the.gr8.regenassist;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main extends JavaPlugin {

    private MVWorldManager worldManager;
    private MultiverseHandler multiverseHandler;
    private MessageHandler msg;
    private ArrayList<String> worlds;


    @Override
    public void onEnable() {
        //get an instance of the MVWorldManager from the Multiverse API
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        worldManager = core.getMVWorldManager();

        //get an instance of the Fancifier class to get fancy messages
        msg = new MessageHandler();

        //pass the MVWorldManager and Fancifier on to the MultiverseHandler class
        multiverseHandler = new MultiverseHandler(worldManager, msg);

        //register the JoinListener
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        //get the worldlist
        //---> use config to get list
        //---> get new list upon config reload
        worlds = new ArrayList<>();
        worlds.add("resourceworld");
        worlds.add("resourceworld_nether");

        getLogger().info("Enabled RegenAssist");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled RegenAssist");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //the main command
        if(label.equalsIgnoreCase("regen")) {

            //check if a worldname is included
            if(args.length == 0) {
                sender.sendMessage(msg.missingName());
                return true;
            }

            //check to see whether world name is a valid option for regen
            //---> check config
            else if(args.length > 0 && worlds.contains(args[0])) {

                //---> ask for confirmation before continuing
                //---> dispatch command from console to send message to player with tellraw clickable link
                long time = Bukkit.getWorld(args[0]).getGameTime();


                String uniqueRegenCmd = "/regenconfirm";
                String confirmCommand = "tellraw "+sender.getName()+ msg.confirm(args[0], uniqueRegenCmd, time);

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), confirmCommand);

                //---> give player 15 seconds to click "confirm"
                return true;
            }
        }

        //hand it over to the multiverseHandler
        if(label.equalsIgnoreCase("regenconfirm")) {
            multiverseHandler.mvRegen(sender, args[0]);
            sender.sendMessage(ChatColor.GOLD+"Looks promising!");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        //show the names of worlds that can be regenerated
        //add config to provide this list
        List<String> finalList = new ArrayList<>();
        if(label.equalsIgnoreCase("regen")) {

            if(args.length == 1) {
                for (String world : worlds) {
                    if(world.startsWith(args[0])) {
                        finalList.add(world);
                    }
                }
            }

            if(args.length == 2) {
                List<String> seedList = new ArrayList<>();
                seedList.add("same-seed");
                seedList.add("random-seed");
                seedList.add("supply-seed:");

                for (String seed : seedList) {
                    if(seed.startsWith(args[1])) {
                        finalList.add(seed);
                    }
                }
            }

            if(args.length == 3) {
                finalList.add("reset-gamerules");
            }
        }

        return finalList;
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
