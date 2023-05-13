package com.radovan.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.radovan.spring.exceptions.BookQuantityException;
import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.InvalidCartException;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.exceptions.ReviewAlreadyExistsException;
import com.radovan.spring.exceptions.SuspendedUserException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(ExistingEmailException.class)
	public ResponseEntity<String> handleExistingEmailException(ExistingEmailException ex) {
		return ResponseEntity.internalServerError().body("Email exists already!");
	}

	@ExceptionHandler(InvalidUserException.class)
	public ResponseEntity<String> handleInvalidUserException(InvalidUserException ex) {
		return ResponseEntity.internalServerError().body("Invalid user!");
	}

	@ExceptionHandler(InvalidCartException.class)
	public ResponseEntity<String> handleInvalidCartException(InvalidCartException ex) {
		return ResponseEntity.internalServerError().body("Invalid cart");
	}

	@ExceptionHandler(SuspendedUserException.class)
	public ResponseEntity<String> handleSuspendedUserException(SuspendedUserException ex) {
		SecurityContextHolder.clearContext();
		return ResponseEntity.internalServerError().body("Account Suspended!");
	}

	@ExceptionHandler(ReviewAlreadyExistsException.class)
	public ResponseEntity<String> handleReviewAlreadyExistsException(ReviewAlreadyExistsException ex) {
		return ResponseEntity.internalServerError().body("Review exists already");
	}

	@ExceptionHandler(BookQuantityException.class)
	public ResponseEntity<String> handleBookQuantityException(BookQuantityException ex) {
		return ResponseEntity.internalServerError().body("Maximum 50 books allowed");
	}
}
