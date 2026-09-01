package com.athenstours;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AthensToursApplication {

    public static void main(String[] args) {
        SpringApplication.run(AthensToursApplication.class, args);
    }
}
