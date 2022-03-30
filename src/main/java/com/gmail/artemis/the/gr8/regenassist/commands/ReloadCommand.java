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
        boolean reloaded = config.reloadFile() && playerFile.reloadFile() && regenFile.reloadFile();
        if (reloaded) {
            sender.sendMessage(MessageWriter.reloadedFiles());
            return true;
        }

        sender.sendMessage(MessageWriter.notReloadedFiles());
        return false;
    }

}
