package com.umc.withme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WithMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WithMeApplication.class, args);
    }

}
