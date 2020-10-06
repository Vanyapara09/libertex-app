package com.parakhnevich.libertex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@EnableRetry
public class AccountAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountAppApplication.class, args);
    }

}
