package com.gmail.artemis.the.gr8.regenassist.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessageWriter {

    public String alreadyRegenerating() {
        return ChatColor.GOLD+"Someone else is already in the process of regenerating this world, please try again later";
    }

    public String mainWorldWarning() {
        return ChatColor.RED+"You cannot regenerate the main world!";
    }

    public String missingName() {
        return ChatColor.RED+"Please specify a world";
    }

    public String missingSeed() {
        return ChatColor.RED + "If you want to supply your own seed, you do have to... you know... actually supply your own seed";
    }

    public String missingSeedOption () {
        return ChatColor.RED + "Please choose an option for the seed";
    }

    public String startRegenerating(String worldName) {
        return ChatColor.GOLD + "Starting the regeneration of the " + ChatColor.AQUA + worldName + ChatColor.GOLD + "...";
    }

    public String doneRegenerating(String worldName) {
        return ChatColor.GREEN + "The " + ChatColor.AQUA + worldName + ChatColor.GREEN + " has been successfully regenerated!";
    }

    public String wrongName() {
        return ChatColor.RED + "This world is not listed as a world that can be regenerated. Double-check your spelling, and check if the world is listed in the config.";
    }


    private String readableTime(long time, String unit) {

        String textTime = unit;

        long seconds = time;
        long minutes = time/60;
        long hours = time/60/60;
        long days = time/60/60/24;

        return time + unit;
    }

    public String confirm(String worldName, String uniqueRegenCmd, long timeSinceRegen) {
        long readableTime = timeSinceRegen;

        return " [\"\",{\"text\":\"[\",\"color\":\"gray\"}," +
                "{\"text\":\"RegenAssist\",\"color\":\"gold\"}," +
                "{\"text\":\"] \",\"color\":\"gray\"}," +
                "{\"text\":\"You are about to regenerate the \",\"color\":\"white\"}," +
                "{\"text\":\""+worldName+"\",\"color\":\"aqua\"}," +
                "{\"text\":\". This world was last regenerated \",\"color\":\"white\"}," +
                "{\"text\":\"["+readableTime+"]\",\"color\":\"gold\"}," +
                "{\"text\":\" ago\",\"color\":\"white\"}," +
                "{\"text\":\". To continue, click \",\"color\":\"white\"}," +
                "{\"text\":\"[confirm]\",\"color\":\"dark_purple\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\""+uniqueRegenCmd+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"[Click to confirm]\",\"italic\":true,\"color\":\"white\"}]}}]";
    }
}
