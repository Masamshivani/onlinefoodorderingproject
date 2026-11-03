package com.example.foodorder.exception;

public class InvalidOrderUpdateException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidOrderUpdateException(String message) {
        super(message);
    }
}
