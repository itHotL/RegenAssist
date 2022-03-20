package com.gmail.artemis.the.gr8.regenassist.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class TimeHandler {

    private TimeHandler() {
    }


    public static LocalDateTime getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return now;
    }

    public static String getStringTimeDifference(String regenTime) {
        long seconds = getTimeDifference(regenTime);

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

    private static long getTimeDifference(String regenDateTime) {
        LocalDateTime now = LocalDateTime.now();

        try {
            LocalDateTime regen = LocalDateTime.parse(regenDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return regen.until(now, ChronoUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
