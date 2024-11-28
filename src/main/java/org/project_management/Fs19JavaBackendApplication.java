package org.project_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
public class Fs19JavaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(Fs19JavaBackendApplication.class, args);
    }
}
