package com.example.danhom1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class Danhom1Application {

	public static void main(String[] args) {
//		Role role = new Role();
		SpringApplication.run(Danhom1Application.class, args);
	}
}
