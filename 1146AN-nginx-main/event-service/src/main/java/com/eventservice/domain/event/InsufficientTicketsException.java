package com.eventservice.domain.event;

/**
 * Domain Exception: Thrown when trying to reserve more tickets than available
 */
public class InsufficientTicketsException extends RuntimeException {
    public InsufficientTicketsException(String message) {
        super(message);
    }
}
