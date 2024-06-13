package ru.bankoperations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProfileApplication {
    public static void main(String[] args) {

        SpringApplication.run(ProfileApplication.class, args);
    }
}