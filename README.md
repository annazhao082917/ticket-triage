# Ticket Triage

A small HTTP API that classifies one support ticket. `POST /api/tickets/triage` checks the subject, description, and optional email, then asks Claude for a category, a priority, a one-sentence summary, a short draft reply, and a confidence score. The response is JSON in the shape of `TriageResult`.

## Tech stack

- Java 17
- Spring Boot 3.5.16
- Spring AI 1.1.8, with the Anthropic starter (Claude model `claude-sonnet-5-5`)
- Maven, via the included wrapper (`./mvnw`)

Spring AI 1.1.8 is the latest stable Spring AI release that runs on Spring Boot 3. Spring AI 2.x requires Spring Boot 4.

## How to run

Set your Anthropic key in the environment, then start the app. The value below is a placeholder, not a real key.

```bash
export ANTHROPIC_API_KEY=your-key-here
./mvnw spring-boot:run
```

Health check: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health). A healthy process returns `{"status":"UP"}`.

### Example request

This curl is an example of the call. It is not a recorded response from Claude.

```bash
curl -s -X POST http://localhost:8080/api/tickets/triage \
  -H 'Content-Type: application/json' \
  -d '{"subject":"Invoice download broken","description":"The PDF link on the billing page returns 404.","customerEmail":"ada@example.com"}'
```

### Example response

This JSON is an example of the shape, not a live answer from the model.

```json
{
  "category": "BILLING",
  "priority": "HIGH",
  "summary": "The customer cannot download an invoice.",
  "suggestedReply": "Sorry the invoice link failed. We are looking at it.",
  "confidence": 0.86
}
```

A blank subject or description returns 400 and a list of field errors. If Claude cannot be reached, the API returns 502 with a generic message.

## How to run tests

```bash
./mvnw verify
```

The tests use a dummy key and a mock of the triage service. They do not call Claude and do not need a real secret.

## Problems I hit and how I solved them

1. Spring AI 2.x requires Spring Boot 4, so this project stays on Spring Boot 3.5.16 and Spring AI 1.1.8. The Spring AI version is set once, in the BOM inside `pom.xml`, and the Anthropic starter does not repeat it.
2. The context-load test needed an API key at startup, because `application.yml` reads `ANTHROPIC_API_KEY` and the Anthropic client refuses a missing key. A `test` profile in `src/test/resources/application-test.yml` supplies a dummy key, so CI never calls Claude or stores a secret.

## Coming soon

- Persistence
- Docker

Done so far: Step 1 (project skeleton), Step 2 (request and response types), and Steps 3–5 (Claude client, `POST /api/tickets/triage`, and the controller test).
