package com.gmail.artemis.the.gr8.regenassist.utils;

import org.bukkit.ChatColor;


public final class MessageWriter {

    private MessageWriter() {
    }

    public static String confirmCommand(String worldName, String uniqueRegenCmd, String timeSinceRegen) {

        String msg = "{\"text\":\"You are about to regenerate world \",\"color\":\"white\"},{\"text\":\"\\\""+worldName+"\\\"\",\"color\":\"aqua\"},{\"text\":\". This world was last regenerated \",\"color\":\"white\"},{\"text\":\"[time]\",\"color\":\"gold\"},{\"text\":\" ago. To continue, click \",\"color\":\"white\"},{\"text\":\"[confirm]\",\"color\":\"dark_purple\"}";
        String time = (timeSinceRegen.equalsIgnoreCase("")) ? "" :
                "{\"text\":\". This world was last regenerated \",\"color\":\"white\"}," +
                "{\"text\":\"["+timeSinceRegen+"]\",\"color\":\"gold\"}," +
                "{\"text\":\" ago\",\"color\":\"white\"},";

        return " [\"\",{\"text\":\"[\",\"color\":\"gray\"}," +
                "{\"text\":\"RegenAssist\",\"color\":\"gold\"}," +
                "{\"text\":\"] \",\"color\":\"gray\"}," +
                "{\"text\":\"You are about to regenerate world \",\"color\":\"white\"}," +
                "{\"text\":\"\\\""+worldName+"\\\"\",\"color\":\"aqua\"}," +time+
                "{\"text\":\". To continue, click \",\"color\":\"white\"}," +
                "{\"text\":\"[confirm]\",\"color\":\"dark_purple\"," +
                "\"clickEvent\":{\"action\":\"run_command\",\"value\":\""+uniqueRegenCmd+"\"}," +
                "\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"[Click to confirm]\",\"italic\":true,\"color\":\"white\"}]}}]";
    }

    public static String confirmCommandConsole(String worldName, String timeSinceRegen) {
        String time = (timeSinceRegen.equalsIgnoreCase("")) ? "" :
                ". This world was last regenerated " + ChatColor.GOLD + timeSinceRegen + ChatColor.RESET + " ago";
        return "You are about to regenerate the " + ChatColor.AQUA + worldName + ChatColor.RESET + time + ". To continue, type /regenconfirm " + worldName;
    }

    public static String teleportMessage() {
        return " [\"\",{\"text\":\"[\",\"color\":\"gray\"}," +
                "{\"text\":\"RegenAssist\",\"color\":\"gold\"}," +
                "{\"text\":\"] \",\"color\":\"gray\"}," +
                "{\"text\":\"The world you were in has been reset since you last played. You've been safely teleported back to spawn :D\",\"color\":\"white\"}]";
    }

    public static String reloadedFiles() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.GREEN + "Successfully reloaded all files!";
    }

    public static String reloadedSomeFiles(boolean reloadedConfig) {
        String basemsg = ChatColor.GOLD + "Some files failed to reload";
        String configmsg = reloadedConfig ? ". The config did reload though!" : ", including the config.";

        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                basemsg+configmsg;
    }

    public static String notReloadedFiles() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.RED + "Something went wrong and the files could not be reloaded.";
    }

    public static String alreadyRegenerating() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.GOLD + "Someone else is already in the process of regenerating this world, please check again later!";
    }

    public static String tooSlow() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.GOLD + "You didn't click confirm fast enough, please repeat your command!";
    }

    public static String missingName() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.RED + "Please specify a world";
    }

    public static String wrongName() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.RED + "This world is not on RegenAssist's list. If you want to regenerate it, add it to the config and use /regenreload.";
    }

    public static String mainWorldWarning() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.RED + "You cannot regenerate your main world! " + "\n" + "(Multiverse cannot unload it)";
    }

    public static String unknownWorld() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.GOLD + "Unable to regen a world that does not exist!";
    }

    public static String missingSeed() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.RED + "If you want to supply your own seed, you do have to... you know... actually supply your own seed";
    }

    public static String missingSeedOption () {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.RED + "Please include an option for the seed";
    }

    public static String startRegenerating() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.GREEN + "Regeneration starting...";
    }

    public static String portalFound() {
        //String pname = portalName != null ? ChatColor.BLUE + "\"" + portalName + "\"" + ChatColor.GREEN + "" : "";
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.GREEN + "Restoring portal...";
    }

    public static String portalError() {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.RED + "Something went wrong, and the portal could not be moved.";
    }

    public static String doneRegenerating(String worldName, boolean portalFixed, String portalName) {
        String pname = portalName != null ? ChatColor.BLUE + "\"" + portalName + "\"" + ChatColor.GREEN + " " : "";
        String portalmsg = portalFixed ? " and portal " + pname  + "has been restored!" : "!";

        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.GREEN + "World " + ChatColor.AQUA + "\"" + worldName + "\"" + ChatColor.GREEN + " has been regenerated" + portalmsg;
    }

    public static String unknownRegenStatus(String worldName) {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.RED + "Unable to confirm whether " + ChatColor.AQUA + "\"" + worldName + "\"" + ChatColor.RED + " has been regenerated.";
    }

    public static String unknownError() {
        return  ChatColor.GRAY + "[" + ChatColor.GOLD + "RegenAssist" + ChatColor.GRAY + "] " +
                ChatColor.RED + "An unknown error occurred, and the regeneration could not go through.";
    }
}
