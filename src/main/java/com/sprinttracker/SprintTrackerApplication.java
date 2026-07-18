package com.sprinttracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SprintTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SprintTrackerApplication.class, args);
    }
}
