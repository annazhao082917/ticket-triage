package com.annazhao.tickettriage.ticket;

/**
 * The triage call to Claude failed, or the model output could not be read.
 *
 * <p>The message is for our logs. Callers see a generic 502 instead, so a
 * stack trace or provider error never reaches the client.
 */
public class TriageException extends RuntimeException {

	public TriageException(String message) {
		super(message);
	}

	public TriageException(String message, Throwable cause) {
		super(message, cause);
	}

}
