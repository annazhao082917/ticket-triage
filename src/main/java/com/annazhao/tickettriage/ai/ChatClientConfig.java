package com.annazhao.tickettriage.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Builds the {@link ChatClient} used for ticket triage.
 *
 * <p>The system prompt is a constant in this class. It has no placeholders, so a
 * separate StringTemplate file would only add a hop between the words and the
 * bean that sends them. Spring AI renders {@code {placeholders}} as templates
 * only when parameters are supplied; this text has none.
 */
@Configuration
public class ChatClientConfig {

	/**
	 * Sent with every triage call. Names match {@code Category} and {@code Priority}.
	 */
	static final String TRIAGE_SYSTEM_PROMPT = """
			You are a support-ticket triage assistant.
			Classify the ticket into exactly one category: BILLING, TECHNICAL, ACCOUNT, FEATURE_REQUEST, or OTHER.
			Classify it into exactly one priority.
			URGENT means the customer is blocked right now, or money or access is at risk.
			HIGH means the customer is stuck and needs a person today.
			MEDIUM means the issue matters and can wait for the regular queue.
			LOW means a question, a small annoyance, or a request with no time pressure.
			Write a one-sentence summary.
			Write a short, polite draft reply that an agent can edit before sending.
			Do not promise a refund, a fix date, or any account change.
			Give an honest confidence between 0.0 and 1.0. Use a lower number when the ticket is vague.
			Never invent account details, order numbers, or actions that were not in the ticket.
			""";

	/**
	 * Spring AI auto-configures a {@link ChatClient.Builder} from the Anthropic chat
	 * model. Each injection point gets its own builder, so this bean can set a
	 * default system prompt without changing any other client.
	 */
	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder.defaultSystem(TRIAGE_SYSTEM_PROMPT).build();
	}

}
