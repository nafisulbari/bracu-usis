package com.nafisulbari.usis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UsisApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsisApplication.class, args);
    }


    @RequestMapping(value = "/")
    public String hello() {
        return "Hello World";
    }

}
