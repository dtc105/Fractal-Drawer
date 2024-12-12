package com.dtc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public Application(){};

    // Start the application
    public void run(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
