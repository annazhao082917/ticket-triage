package com.annazhao.tickettriage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the ticket triage API.
 *
 * <p>{@code @SpringBootApplication} does three jobs: scan this package (and
 * packages under it) for Spring beans, turn on auto-configuration, and register
 * this class as a source of settings. There are no controllers yet. This step
 * only boots the framework so later steps have a place to live.
 */
@SpringBootApplication
public class TicketTriageApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketTriageApplication.class, args);
	}

}
