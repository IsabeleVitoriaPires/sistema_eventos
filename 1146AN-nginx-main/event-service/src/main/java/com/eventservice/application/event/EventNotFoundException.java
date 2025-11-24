package com.eventservice.application.event;

/**
 * Application Exception: Thrown when an event is not found
 */
public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}
