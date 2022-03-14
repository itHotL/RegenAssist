package com.gmail.artemis.the.gr8.regenassist;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Main extends JavaPlugin {

    private MVWorldManager worldManager;
    private MultiverseHandler multiverseHandler;
    private MessageHandler msg;
    private ArrayList<String> worlds;
    private HashMap<UUID, String> worldCodes = new HashMap<UUID, String>();


    @Override
    public void onEnable() {
        //get an instance of the MVWorldManager from the Multiverse API
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        if(core == null) {
            getLogger().severe("Multiverse-Core not found, RegenAssist cannot live without it </3");
            return;
        }
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

            //check if option for seed was chosen
            else if (args.length == 1) {
                sender.sendMessage(msg.missingSeedOption());
                return false;
            }

            //check if seed was supplied if supply-seed was chosen
            else if (args.length >= 2 && args[1].equalsIgnoreCase("supply-seed:")) {
                sender.sendMessage(msg.missingSeed());
                return false;
            }

            //check whether world name is a valid option for regen
            //---> check config
            else {

                if(!worlds.contains(args[0])) {
                    sender.sendMessage(msg.wrongName());
                    return true;
                }

                if(worlds.contains(args[0])) {
                    long time = Bukkit.getWorld(args[0]).getGameTime();

                    //put unique confirm-code on HashMap attached to the worldName (if there is no entry for this name yet)
                    if(worldCodes.containsValue(args[0])) {
                        sender.sendMessage(msg.alreadyRegenerating());
                    }

                    else {
                        UUID uniqueCode = UUID.randomUUID();
                        worldCodes.put(uniqueCode, args[0]);

                        //start 15 second timer that removes unique code from the HashMap if it is still there
                        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                            @Override
                            public void run() {
                                if(worldCodes.containsKey(uniqueCode)) {
                                    worldCodes.remove(uniqueCode);
                                }
                            }
                        }, 300L);

                        //give confirm prompt to player
                        String gamerules = (args.length == 3) ? args[2] : " ";
                        String uniqueRegenCmd = "/regenconfirm "+uniqueCode+" "+args[1]+" "+gamerules;
                        String confirmCommand = "tellraw "+sender.getName()+ msg.confirm(args[0], uniqueRegenCmd, time);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), confirmCommand);
                    }

                    return true;
                }
            }
        }

        //if player clicks confirm within 15 seconds, get worldName from HashMap and pass it on to the MultiverseHandler
        if(label.equalsIgnoreCase("regenconfirm")) {

            //args[0] = uniqueCode
            //args[1] = same-seed/random-seed/supply-seed:
            //args[2] = optional reset-gamerules
            if (args.length >= 2 && worldCodes.containsKey(UUID.fromString(args[0]))) {

                String worldName = worldCodes.remove(UUID.fromString(args[0]));
                boolean useNewSeed = (args[1].equalsIgnoreCase("random-seed") || args[1].startsWith("supply-seed:"));
                boolean randomSeed = (args[1].equalsIgnoreCase("random-seed"));
                String seed = args[1].startsWith("supply-seed:") ? args[1].substring(12) : "";
                boolean keepGameRules = !(args.length == 3 && args[2].equalsIgnoreCase("reset-gamerules"));

                //start the regen
                sender.sendMessage(msg.startRegenerating(worldName));
                multiverseHandler.mvRegen(sender, worldName, useNewSeed, randomSeed, seed, keepGameRules);

                //check every second if the world has been loaded again after regenerating, and stop + give feedback when the world has been loaded
                new BukkitRunnable() {
                    public void run() {
                        if (!worldManager.getUnloadedWorlds().contains(worldName)) {
                            sender.sendMessage(msg.doneRegenerating(worldName));
                            this.cancel();
                        }
                    }
                }.runTaskTimer(this, 20L, 20L);
            }

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
