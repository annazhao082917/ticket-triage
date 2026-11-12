package com.annazhao.tickettriage.ticket;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP entry point for triage. Validation runs before the service is called.
 */
@RestController
@RequestMapping("/api/tickets")
public class TriageController {

	private final TriageService triageService;

	public TriageController(TriageService triageService) {
		this.triageService = triageService;
	}

	/**
	 * Accepts one ticket and returns Claude's classification.
	 */
	@PostMapping("/triage")
	public TriageResult triage(@Valid @RequestBody TicketRequest request) {
		return this.triageService.triage(request);
	}

}
