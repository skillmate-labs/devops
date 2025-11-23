package com.skillmate.skillmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkillmateApplication {

	public static void main(String[] args) {
		System.out.println("Starting Skillmate Application...");
		SpringApplication.run(SkillmateApplication.class, args);
		System.out.println("Application started successfully!");
	}

}
