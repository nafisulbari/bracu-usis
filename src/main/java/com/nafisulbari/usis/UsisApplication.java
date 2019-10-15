package com.nafisulbari.usis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class UsisApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsisApplication.class, args);
    }

}
