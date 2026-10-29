package com.annazhao.tickettriage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Boots the full Spring context and stops there.
 *
 * <p>The {@code test} profile supplies a dummy Anthropic key, so this passes
 * with no real API key and without calling Claude.
 */
@SpringBootTest
@ActiveProfiles("test")
class TicketTriageApplicationTests {

	@Test
	void contextLoads() {
	}

}
