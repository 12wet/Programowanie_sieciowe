package com.exercise3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Exercise3Application {
	/*======================================================================*/
	// CHANGE THE VARIABLE TO DISCOGS ARTIST ID
	// OF YOUR CHOICE
	final static int DESIRED_ID = 359282;
	/*======================================================================*/


	public static void main(String[] args) {
		SpringApplication.run(Exercise3Application.class, args);
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) {
		return args -> GroupSearcher.search(restTemplate.getForObject(
				GroupSearcher.getApiUrl() + DESIRED_ID, Group.class));
	}
}