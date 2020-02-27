package com.iceolive.httpmock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication

public class HttpmockApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpmockApplication.class, args);
    }

}
