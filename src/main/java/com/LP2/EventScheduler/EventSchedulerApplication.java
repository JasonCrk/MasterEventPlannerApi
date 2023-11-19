package com.LP2.EventScheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAsync
@SpringBootApplication
public class EventSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventSchedulerApplication.class, args);
	}

}
