package com.annazhao.tickettriage.web;

import java.util.LinkedHashMap;
import java.util.Map;

import com.annazhao.tickettriage.ticket.TriageException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Turns failures into a small JSON body. Field errors are safe to show.
 * Anything from Claude stays in the log.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	/**
	 * A request that failed {@code @Valid}. Lists each field and its message.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				"The ticket request is invalid.");
		Map<String, String> errors = new LinkedHashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.putIfAbsent(fieldError.getField(),
				fieldError.getDefaultMessage()));
		problem.setProperty("errors", errors);
		return problem;
	}

	/**
	 * Claude was unreachable, or its answer could not be parsed. The response
	 * is generic. The cause is logged here and is not copied into the body.
	 */
	@ExceptionHandler(TriageException.class)
	public ProblemDetail handleTriage(TriageException ex) {
		log.error("Triage call failed", ex);
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY,
				"The triage service is unavailable. Please try again.");
	}

}
