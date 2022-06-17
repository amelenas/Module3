package com.epam.esm;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class WebApplication {

    public static void main(String[] args) {

        new SpringApplicationBuilder(WebApplication.class)
               .profiles("prod")
               .run(args);
    }
}