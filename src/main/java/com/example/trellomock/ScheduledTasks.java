package com.example.trellomock;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    //@Scheduled(cron = "* * * * * ?")
    public static void scheduleTaskWithCronExpression() {
        logger.info("Cron Task: Current Time - {}", formatter.format(LocalDateTime.now()));
    }
}
