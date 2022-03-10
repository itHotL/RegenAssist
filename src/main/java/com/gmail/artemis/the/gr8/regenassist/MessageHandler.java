package com.gmail.artemis.the.gr8.regenassist;

import org.bukkit.ChatColor;

public class MessageHandler {

    public String missingName() {
        return ChatColor.RED+"Please specify a world";
    }

    public String worldWarning() {
        return ChatColor.RED+"You cannot regenerate the main world!";
    }

    public String confirm(String worldName) {
        return " [\"\",{\"text\":\"[\",\"color\":\"gray\"}," +
                "{\"text\":\"RegenAssist\",\"color\":\"gold\"}," +
                "{\"text\":\"] \",\"color\":\"gray\"}," +
                "{\"text\":\"You are about to regenerate the \",\"color\":\"white\"}," +
                "{\"text\":\""+worldName+"\",\"color\":\"green\"}," +
                "{\"text\":\". This world was last regenerated \",\"color\":\"white\"}," +
                "{\"text\":\"[time]\",\"color\":\"gold\"}," +
                "{\"text\":\" ago\",\"color\":\"white\"}," +
                "{\"text\":\". To continue, click \",\"color\":\"white\"}," +
                "{\"text\":\"[confirm]\",\"color\":\"dark_purple\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/regenconfirm\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"[Click to confirm]\",\"italic\":true,\"color\":\"white\"}]}}]";
    }
}
