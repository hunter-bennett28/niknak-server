package com.hunterbennett.niknak.niknakserver.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NikNakUtils {
    public static String getCurrentDateString() {
        LocalDateTime date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ofPattern("M/D/yyyy H:mm:ss a"));
    }
}
