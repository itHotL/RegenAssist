package com.gmail.artemis.the.gr8.regenassist;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessageHandler {

    public String missingName() {
        return ChatColor.RED+"Please specify a world";
    }

    public String worldWarning() {
        return ChatColor.RED+"You cannot regenerate the main world!";
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
                "{\"text\":\""+worldName+"\",\"color\":\"green\"}," +
                "{\"text\":\". This world was last regenerated \",\"color\":\"white\"}," +
                "{\"text\":\"["+readableTime+"]\",\"color\":\"gold\"}," +
                "{\"text\":\" ago\",\"color\":\"white\"}," +
                "{\"text\":\". To continue, click \",\"color\":\"white\"}," +
                "{\"text\":\"[confirm]\",\"color\":\"dark_purple\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\""+uniqueRegenCmd+"\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"[Click to confirm]\",\"italic\":true,\"color\":\"white\"}]}}]";
    }
}
