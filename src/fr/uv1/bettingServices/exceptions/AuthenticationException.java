package fr.uv1.bettingServices.exceptions;

/**
 */ 

public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(String message) {
		super(message);
	}
}
