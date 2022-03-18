package com.gmail.artemis.the.gr8.regenassist.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class TimeHandler {

    private TimeHandler() {
    }

    public static LocalDateTime getExampleDate() {
        LocalDateTime ex = LocalDateTime.now().minusHours(6);
        return ex;
    }

    public static LocalDateTime getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return now;
    }

    public static long getTimeDifference(String regenTime) {
        LocalDateTime regenDate = LocalDateTime.parse(regenTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime now = LocalDateTime.now();


        long diff = regenDate.until(now, ChronoUnit.MINUTES);
        return diff;
    }
}
