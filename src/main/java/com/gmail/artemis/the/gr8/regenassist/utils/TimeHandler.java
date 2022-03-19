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
        long mins = getTimeDifference(regenTime);

        if (mins>=1440) {
            long days = Math.round(mins/60/24);
            return (days==1) ? (days + " day") : (days + " days");
        }

        else if (mins>=60) {
            long hours = Math.round(mins/60);
            return (hours==1) ? (hours + " hour") : (hours + " hours");
        }

        else if (mins==0) {
            return "";
        }

        else {
            return (mins==1) ? (mins + " minute") : (mins + " minutes");
        }
    }

    private static long getTimeDifference(String regenDateTime) {
        LocalDateTime now = LocalDateTime.now();

        try {
            LocalDateTime regen = LocalDateTime.parse(regenDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return regen.until(now, ChronoUnit.MINUTES);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
