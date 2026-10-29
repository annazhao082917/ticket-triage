package com.annazhao.tickettriage.ticket;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Body a client will POST when it asks us to triage one ticket.
 *
 * <p>The constraints sit on this type, at the edge of the API. A later
 * controller will reject a bad body before any triage code runs. A missing
 * email is allowed; an email that is present must look like an address.
 */
public record TicketRequest(
		/** Short title. Required, at most 200 characters. */
		@NotBlank @Size(max = 200) String subject,
		/** What the customer needs. Required, at most 5000 characters. */
		@NotBlank @Size(max = 5000) String description,
		/** Optional. When present, must be an email address. */
		@Email String customerEmail) {
}
