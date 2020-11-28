package com.example.trellomock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateOps {
    public static Duration DateDiff(String date1, String date2){
        System.out.println(date2);
        date1.replace('T', ' ');
        date2.replace('T', ' ');

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dt1 = LocalDateTime.parse(date1,formatter);
        LocalDateTime dt2 = LocalDateTime.parse(date2,formatter);

        Duration diff = Duration.between(dt1,dt2);
        return diff;
    }
}
