package com.annazhao.tickettriage.ticket;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises the HTTP layer with a fake {@link TriageService}. Claude is never
 * called: {@code @WebMvcTest} does not start the chat client.
 */
@WebMvcTest(TriageController.class)
class TriageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TriageService triageService;

	@Test
	void validTicketReturnsTriageResult() throws Exception {
		when(this.triageService.triage(any(TicketRequest.class))).thenReturn(new TriageResult(Category.BILLING,
				Priority.HIGH, "The customer cannot download an invoice.",
				"Sorry the invoice link failed. We are looking at it.", 0.86));

		this.mockMvc
			.perform(post("/api/tickets/triage").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"subject":"Invoice download broken","description":"The PDF link returns 404.","customerEmail":"ada@example.com"}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.category").value("BILLING"))
			.andExpect(jsonPath("$.priority").value("HIGH"));
	}

	@Test
	void blankSubjectIsRejectedBeforeTheService() throws Exception {
		this.mockMvc
			.perform(post("/api/tickets/triage").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"subject":"   ","description":"The app crashes when I open settings.","customerEmail":"ada@example.com"}
						"""))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.errors.subject", containsString("blank")));

		verify(this.triageService, never()).triage(any());
	}

	@Test
	void serviceFailureReturnsBadGateway() throws Exception {
		when(this.triageService.triage(any(TicketRequest.class)))
			.thenThrow(new TriageException("Claude triage failed", new IllegalStateException("connection reset by anthropic")));

		this.mockMvc
			.perform(post("/api/tickets/triage").contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"subject":"Password reset","description":"The reset email never arrived."}
						"""))
			.andExpect(status().isBadGateway())
			.andExpect(jsonPath("$.detail").value("The triage service is unavailable. Please try again."))
			.andExpect(content().string(not(containsString("connection reset"))));
	}

}
