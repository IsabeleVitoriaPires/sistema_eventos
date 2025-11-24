package com.ticketsystem.domain.purchase;

/**
 * Purchase Status Enum
 */
public enum PurchaseStatus {
    PENDING,    // Purchase created but not yet confirmed
    CONFIRMED,  // Purchase confirmed and tickets issued
    CANCELLED   // Purchase cancelled and tickets invalidated
}
