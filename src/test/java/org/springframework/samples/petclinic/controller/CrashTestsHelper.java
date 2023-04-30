package org.springframework.samples.petclinic.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Test helper class for handling the exception nested.
 *
 * @author Amy Qi Wang
 *
 */
@ControllerAdvice
public class CrashTestsHelper {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object exceptionHandler(Exception e) {
		return new HttpEntity<>(e.getMessage());
	}

}
