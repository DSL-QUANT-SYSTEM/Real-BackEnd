package com.example.BeFETest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BeFeTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeFeTestApplication.class, args);
	}

}
