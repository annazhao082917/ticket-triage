package com.annazhao.tickettriage.ticket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Structured answer we will ask Claude to fill in later.
 *
 * <p>{@code ChatClient} can map the model output onto this type with
 * {@code .entity(TriageResult.class)}. Spring AI builds the JSON schema from
 * the component names and from {@link JsonPropertyDescription}. The Javadoc
 * is for people reading the code. The annotation is what the model sees.
 */
@JsonClassDescription("Classification of one support ticket.")
public record TriageResult(
		/**
		 * Which queue this ticket belongs in.
		 */
		@JsonPropertyDescription("Ticket bucket. One of BILLING, TECHNICAL, ACCOUNT, FEATURE_REQUEST, OTHER.")
		Category category,

		/**
		 * How quickly a person should look at this ticket.
		 */
		@JsonPropertyDescription("How fast a person should respond. One of LOW, MEDIUM, HIGH, URGENT.")
		Priority priority,

		/**
		 * One sentence a teammate can scan.
		 */
		@JsonPropertyDescription("One sentence describing the customer's problem.")
		String summary,

		/**
		 * Draft the agent can edit. Not sent to the customer automatically.
		 */
		@JsonPropertyDescription("A draft reply to the customer, ready for a person to edit and send.")
		String suggestedReply,

		/**
		 * How sure the model is, from 0.0 (a guess) to 1.0 (certain).
		 */
		@JsonPropertyDescription("How sure you are, from 0.0 (a guess) to 1.0 (certain).")
		double confidence) {
}
