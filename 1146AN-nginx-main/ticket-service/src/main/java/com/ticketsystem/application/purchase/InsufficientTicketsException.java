package com.ticketsystem.application.purchase;

/**
 * Application Exception: Thrown when there are not enough tickets available
 */
public class InsufficientTicketsException extends RuntimeException {
    public InsufficientTicketsException(String message) {
        super(message);
    }
}
