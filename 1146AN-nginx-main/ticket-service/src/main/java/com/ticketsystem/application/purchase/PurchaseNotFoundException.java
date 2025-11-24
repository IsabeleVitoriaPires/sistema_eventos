package com.ticketsystem.application.purchase;

/**
 * Application Exception: Thrown when a purchase is not found
 */
public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException(String message) {
        super(message);
    }
}
