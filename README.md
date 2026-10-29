# Ticket Triage

A small HTTP API that will sort incoming support tickets. This repository is the Day 1 skeleton: a Spring Boot app that can start, report its health, and is ready to call Claude. Endpoints, the triage logic, a database, and Docker come in later steps.

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

Then open [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health). A healthy process returns `{"status":"UP"}`.

Check the skeleton without a real key:

```bash
./mvnw verify
```

## Coming soon

- Ticket submission endpoints
- A triage service that asks Claude to classify a ticket
- Persistence
- Docker
