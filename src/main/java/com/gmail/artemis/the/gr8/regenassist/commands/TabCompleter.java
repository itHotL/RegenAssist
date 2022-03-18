package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.utils.ConfigHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    private Utilities utils;
    private ConfigHandler conf;

    public TabCompleter (Utilities u, ConfigHandler c) {
        utils = u;
        conf = c;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        //show the names of worlds that can be regenerated
        List<String> finalList = new ArrayList<>();
        if(label.equalsIgnoreCase("regen")) {

            if(args.length == 1) {
                for (String world : conf.getWorldList()) {
                    if(world.startsWith(args[0])) {
                        finalList.add(world);
                    }
                }
            }

            //show the different options for seeds
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

            //show the final (optional) argument
            if(args.length == 3) {
                finalList.add("reset-gamerules");
            }
        }

        return finalList;
    }
}
