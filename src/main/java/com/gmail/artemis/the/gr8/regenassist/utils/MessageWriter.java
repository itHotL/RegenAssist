package com.gmail.artemis.the.gr8.regenassist.utils;

import org.bukkit.ChatColor;


public final class MessageWriter {

    private MessageWriter() {

    }

    public static String alreadyRegenerating() {
        return ChatColor.GOLD+"Someone else is already in the process of regenerating this world, please try again later";
    }

    public static String mainWorldWarning() {
        return ChatColor.RED+"You cannot regenerate the main world!";
    }

    public static String missingName() {
        return ChatColor.RED+"Please specify a world";
    }

    public static String missingSeed() {
        return ChatColor.RED + "If you want to supply your own seed, you do have to... you know... actually supply your own seed";
    }

    public static String missingSeedOption () {
        return ChatColor.RED + "Please choose an option for the seed";
    }

    public static String startRegenerating(String worldName) {
        return ChatColor.GOLD + "Starting the regeneration of the " + ChatColor.AQUA + worldName + ChatColor.GOLD + "...";
    }

    public static String doneRegenerating(String worldName) {
        return ChatColor.GREEN + "The " + ChatColor.AQUA + worldName + ChatColor.GREEN + " has been successfully regenerated!";
    }

    public static String wrongName() {
        return ChatColor.RED + "This world is not listed as a world that can be regenerated. Double-check your spelling, and check if the world is listed in the config.";
    }


    public static String confirm(String worldName, String uniqueRegenCmd, String timeSinceRegen) {

        String time = (timeSinceRegen.equalsIgnoreCase("")) ? "" : "{\"text\":\". This world was last regenerated \",\"color\":\"white\"}," +
                                                    "{\"text\":\"["+timeSinceRegen+"]\",\"color\":\"gold\"}," +
                                                    "{\"text\":\" ago\",\"color\":\"white\"},";

        return " [\"\",{\"text\":\"[\",\"color\":\"gray\"}," +
                "{\"text\":\"RegenAssist\",\"color\":\"gold\"}," +
                "{\"text\":\"] \",\"color\":\"gray\"}," +
                "{\"text\":\"You are about to regenerate the \",\"color\":\"white\"}," +
                "{\"text\":\""+worldName+"\",\"color\":\"aqua\"}," +time+
                "{\"text\":\". To continue, click \",\"color\":\"white\"}," +
                "{\"text\":\"[confirm]\",\"color\":\"dark_purple\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\""+uniqueRegenCmd+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"[Click to confirm]\",\"italic\":true,\"color\":\"white\"}]}}]";
    }
}
