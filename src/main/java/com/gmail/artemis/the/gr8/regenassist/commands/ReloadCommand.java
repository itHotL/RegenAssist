package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.PlayerFileHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageWriter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final ConfigHandler config;
    private final PlayerFileHandler playerFile;
    private final RegenFileHandler regenFile;

    public ReloadCommand (ConfigHandler c, PlayerFileHandler pl, RegenFileHandler r) {
        config = c;
        regenFile = r;
        playerFile = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean rConfig = config.reloadFile();
        boolean rPlayerFile = playerFile.reloadFile();
        boolean rRegenFile = regenFile.reloadFile();

        //if all files have been reloaded
        if (rConfig && rPlayerFile && rRegenFile) {
            sender.sendMessage(MessageWriter.reloadedFiles());
            return true;
        }

        //if some files have been reloaded
        else if (rConfig || rPlayerFile || rRegenFile) {
            sender.sendMessage(MessageWriter.reloadedSomeFiles(rConfig));
            return true;
        }

        //if no files have been reloaded
        else {
            sender.sendMessage(MessageWriter.notReloadedFiles());
            return false;
        }
    }
}
