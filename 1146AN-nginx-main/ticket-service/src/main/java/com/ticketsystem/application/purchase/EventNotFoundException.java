package com.ticketsystem.application.purchase;

/**
 * Application Exception: Thrown when an event is not found in event-service
 */
public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
