package com.gmail.artemis.the.gr8.regenassist.commands;

import com.gmail.artemis.the.gr8.regenassist.Main;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.ConfigHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.PlayerFileHandler;
import com.gmail.artemis.the.gr8.regenassist.filehandlers.RegenFileHandler;
import com.gmail.artemis.the.gr8.regenassist.utils.MessageWriter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final ConfigHandler config;
    private final PlayerFileHandler playerfile;
    private final RegenFileHandler regenfile;

    public ReloadCommand (ConfigHandler c, PlayerFileHandler pl, RegenFileHandler r) {
        config = c;
        regenfile = r;
        playerfile = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean reloaded = config.reloadDataFile() && playerfile.reloadDataFile() && regenfile.reloadDataFile();
        if (reloaded) {
            sender.sendMessage(MessageWriter.reloadedFiles());
            return true;
        }

        sender.sendMessage(MessageWriter.notReloadedFiles());
        return false;
    }

}
