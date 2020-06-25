package com.schooldevops.springboot.event.eventdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EventDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventDemoApplication.class, args);
	}

}
