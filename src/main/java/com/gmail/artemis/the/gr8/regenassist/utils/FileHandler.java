package com.gmail.artemis.the.gr8.regenassist.utils;

import com.gmail.artemis.the.gr8.regenassist.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

    private File dataFile;
    private final Main plugin;
    private boolean append;

    public FileHandler(Main p) {
        plugin = p;
    }

    //write data to a file
    public void writeToFile() {

        dataFile = new File(plugin.getDataFolder(), "data.txt");
        createDataFile();

        try {
            append = true;
            FileWriter myWriter = new FileWriter(dataFile, append);
            myWriter.write("Hello world :D");
            myWriter.flush();
            myWriter.close();
            plugin.getLogger().info("Successfully wrote to the file");
        }
        catch (IOException e) {
            plugin.getLogger().warning("An error occurred");
            e.printStackTrace();
        }
    }

    //create a file to write world- and player-data to if none exists yet
    private void createDataFile() {
        dataFile.getParentFile().mkdirs();
        try {
            dataFile.createNewFile();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

