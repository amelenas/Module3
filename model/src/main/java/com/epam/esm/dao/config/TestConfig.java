package com.epam.esm.dao.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication(scanBasePackages = "com.epam.esm")
@EntityScan({"com.epam.esm.dao"})
public class TestConfig {

}
