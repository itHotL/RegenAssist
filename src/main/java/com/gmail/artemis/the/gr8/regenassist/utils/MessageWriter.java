package com.gmail.artemis.the.gr8.regenassist.utils;

import org.bukkit.ChatColor;


public final class MessageWriter {

    private MessageWriter() {
    }

    public static String confirmCommand(String worldName, String uniqueRegenCmd, String timeSinceRegen) {
        String time = (timeSinceRegen.equalsIgnoreCase("")) ? "" :
                "{\"text\":\". This world was last regenerated \",\"color\":\"white\"}," +
                "{\"text\":\"["+timeSinceRegen+"]\",\"color\":\"gold\"}," +
                "{\"text\":\" ago\",\"color\":\"white\"},";

        return " [\"\",{\"text\":\"[\",\"color\":\"gray\"}," +
                "{\"text\":\"RegenAssist\",\"color\":\"gold\"}," +
                "{\"text\":\"] \",\"color\":\"gray\"}," +
                "{\"text\":\"You are about to regenerate the \",\"color\":\"white\"}," +
                "{\"text\":\""+worldName+"\",\"color\":\"aqua\"}," +time+
                "{\"text\":\". To continue, click \",\"color\":\"white\"}," +
                "{\"text\":\"[confirm]\",\"color\":\"dark_purple\"," +
                "\"clickEvent\":{\"action\":\"run_command\",\"value\":\""+uniqueRegenCmd+"\"}," +
                "\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"[Click to confirm]\",\"italic\":true,\"color\":\"white\"}]}}]";
    }

    public static String teleportMessage() {
        return " [\"\",{\"text\":\"[\",\"color\":\"gray\"}," +
                "{\"text\":\"RegenAssist\",\"color\":\"gold\"}," +
                "{\"text\":\"] \",\"color\":\"gray\"}," +
                "{\"text\":\"The world you were in has been reset since you last played. You've been safely teleported back to spawn :D\",\"color\":\"white\"}]";
    }

    public static String reloadedFiles() {
        return ChatColor.GREEN + "Successfully reloaded all files!";
    }

    public static String reloadedSomeFiles(boolean reloadedConfig) {
        String basemsg = ChatColor.GOLD + "Some files failed to reload";
        String configmsg = reloadedConfig ? ". The config did reload though!" : ", including the config.";

        return basemsg+configmsg;
    }

    public static String notReloadedFiles() {
        return ChatColor.RED + "Something went wrong and the files could not be reloaded.";
    }

    public static String alreadyRegenerating() {
        return ChatColor.GOLD + "Someone else is already in the process of regenerating this world, please check again later!";
    }

    public static String tooSlow() {
        return ChatColor.GOLD + "You didn't click confirm fast enough, please repeat your command!";
    }

    public static String missingName() {
        return ChatColor.RED + "Please specify a world";
    }

    public static String wrongName() {
        return ChatColor.RED + "This world is not on RegenAssist's list. If you want to regenerate it, add it to the config and use /regenreload.";
    }

    public static String mainWorldWarning() {
        return ChatColor.RED + "You cannot regenerate your main world! " + "\n" + "(Multiverse cannot unload it)";
    }

    public static String unknownWorld() {
        return ChatColor.GOLD + "Unable to regen a world that does not exist!";
    }

    public static String missingSeed() {
        return ChatColor.RED + "If you want to supply your own seed, you do have to... you know... actually supply your own seed";
    }

    public static String missingSeedOption () {
        return ChatColor.RED + "Please include an option for the seed";
    }

    public static String startRegenerating(String worldName) {
        return ChatColor.GREEN + "Starting the regeneration of the " + ChatColor.AQUA + worldName + ChatColor.GREEN + "...";
    }

    public static String doneRegenerating(String worldName) {
        return ChatColor.GREEN + "The " + ChatColor.AQUA + worldName + ChatColor.GREEN + " has been successfully regenerated!";
    }

    public static String unknownError(String worldName) {
        return ChatColor.RED + "Something went wrong, and the " + worldName + " could not be regenerated.";
    }
}
