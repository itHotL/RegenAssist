package com.gmail.artemis.the.gr8.regenassist.portal;

import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import com.onarandombox.MultiversePortals.utils.PortalManager;

import java.util.List;

public class MVPortalsHandler {

    private final MultiversePortals mvpAPI;
    private final PortalManager portalManager;

    public MVPortalsHandler(MultiversePortals mvp, PortalManager pm) {
        mvpAPI = mvp;
        portalManager = pm;
    }

    public List<MVPortal> getAllPortals() {
        return portalManager.getAllPortals();
    }

    public boolean setPortalLocation(MVPortal portal, String location, String worldName) {
        return portal.setPortalLocation(location, worldName);
    }

    public boolean savePortalsConfig() {
        return mvpAPI.savePortalsConfig();
    }

    public void reloadConfigs() {
        mvpAPI.reloadConfigs();
    }

}
