package com.annazhao.tickettriage.ticket;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks {@link TicketRequest} with a plain validator. No Spring context:
 * these rules belong to the type itself.
 */
class TicketRequestValidationTest {

	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void validRequestHasNoViolations() {
		TicketRequest request = new TicketRequest(
				"Cannot download invoice",
				"The PDF link on the billing page returns a 404.",
				"ada@example.com");

		assertTrue(validate(request).isEmpty(), "a complete request should pass");
	}

	@Test
	void blankSubjectFails() {
		// @NotBlank treats whitespace-only text as blank.
		TicketRequest request = new TicketRequest(
				"   ",
				"The app crashes when I open settings.",
				"ada@example.com");

		assertTrue(violates(request, "subject"), "a blank subject should fail");
	}

	@Test
	void blankDescriptionFails() {
		TicketRequest request = new TicketRequest("App crash", "   ", "ada@example.com");

		assertTrue(violates(request, "description"), "a blank description should fail");
	}

	@Test
	void badEmailFails() {
		TicketRequest request = new TicketRequest(
				"Password reset",
				"The reset email never arrived.",
				"not-an-email");

		assertTrue(violates(request, "customerEmail"), "a malformed email should fail");
	}

	@Test
	void nullEmailIsAllowed() {
		TicketRequest request = new TicketRequest(
				"Add dark mode",
				"Please add a dark theme to the dashboard.",
				null);

		assertTrue(validate(request).isEmpty(), "a missing email should pass");
	}

	private static Set<ConstraintViolation<TicketRequest>> validate(TicketRequest request) {
		return VALIDATOR.validate(request);
	}

	private static boolean violates(TicketRequest request, String property) {
		return validate(request).stream()
				.anyMatch(violation -> violation.getPropertyPath().toString().equals(property));
	}

}
