package com.annazhao.tickettriage.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * Asks Claude to classify one ticket.
 *
 * <p>The call is the Spring AI 1.1 fluent API:
 * {@code prompt().user(...).call().entity(TriageResult.class)}.
 * {@code entity} builds a JSON schema from {@link TriageResult} and parses
 * the model text back into that record.
 */
@Service
public class TriageService {

	private static final Logger log = LoggerFactory.getLogger(TriageService.class);

	private final ChatClient chatClient;

	public TriageService(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	/**
	 * Classify {@code request}. The customer email stays here; the model only
	 * sees the subject and description.
	 */
	public TriageResult triage(TicketRequest request) {
		log.info("Triaging ticket subjectLength={}", request.subject().length());
		TriageResult result;
		try {
			// entity() is @Nullable in Spring AI 1.1.8. An empty answer is a failure.
			result = this.chatClient.prompt()
				.user(userText(request))
				.call()
				.entity(TriageResult.class);
		}
		catch (RuntimeException ex) {
			throw new TriageException("Claude triage failed", ex);
		}
		if (result == null) {
			throw new TriageException("Claude returned no triage result");
		}
		log.info("Triage finished category={} priority={}", result.category(), result.priority());
		return result;
	}

	private static String userText(TicketRequest request) {
		return """
				Subject: %s

				Description:
				%s
				""".formatted(request.subject(), request.description());
	}

}
