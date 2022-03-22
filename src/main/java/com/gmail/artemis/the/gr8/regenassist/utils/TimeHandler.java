package com.gmail.artemis.the.gr8.regenassist.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class TimeHandler {

    private TimeHandler() {
    }


    public static Instant getCurrentTime() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static String getStringTimeSinceLastRegen(String regenTime) {
        long seconds = getTimeSinceLastRegen(getLastRegenTime(regenTime));

        if (seconds>=86400) {
            int days = Math.round(seconds/60/60/24);
            return (days ==1) ? (days + " day") : (days + " days");
        }

        else if (seconds>=3600) {
            int hours = Math.round(seconds/60/60);
            return (hours==1) ? (hours + " hour") : (hours + " hours");
        }

        else if (seconds>=60) {
            int minutes = Math.round(seconds/60);
            return (minutes==1) ? (minutes + " minute") : (minutes + " minutes");
        }

        else if (seconds==0) {
            return "";
        }

        else {
            return (seconds==1) ? (seconds + " second") : (seconds + " seconds");
        }
    }

    //checks if lastPlayed is before a reset (param: unix time lastPlayed, datafile String lastRegen)
    public static boolean lastPlayedBeforeReset (long unixLastPlayed, String lastRegen) {
        Instant lastPlayed = getLastPlayedTime(unixLastPlayed);
        Instant lastRegenTime = getLastRegenTime(lastRegen);
        return lastRegenTime != null && lastPlayed.isBefore(lastRegenTime);
    }

    private static Instant getLastPlayedTime (long unixTime) {
        return Instant.ofEpochSecond(unixTime/1000);
    }

    private static Instant getLastRegenTime (String lastRegen) {
        try {
            return Instant.parse(lastRegen);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static long getTimeSinceLastRegen (Instant regenInstant) {
        return (regenInstant == null) ? 0 : regenInstant.until(Instant.now(), ChronoUnit.SECONDS);
    }
}
