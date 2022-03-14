package com.gmail.artemis.the.gr8.regenassist.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Utilities {

    private ArrayList<String> worlds = new ArrayList<>();
    private HashMap<UUID, String> worldCodes = new HashMap<>();

    public ArrayList<String> getWorldList() {
        //---> get the worldlist from the config
        //---> get new list upon config reload (does it automatically get passed on to the command executor classes in the constructors I made there?)
        worlds.add("resourceworld");
        worlds.add("resourceworld_nether");

        return worlds;
    }

    public HashMap<UUID, String> getWorldCodes() {

        return worldCodes;
    }
}



