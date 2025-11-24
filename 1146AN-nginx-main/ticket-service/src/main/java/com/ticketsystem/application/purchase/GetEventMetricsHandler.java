package com.ticketsystem.application.purchase;

import com.ticketsystem.domain.purchase.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Use Case: Get sales metrics for an event
 * Requisito 6: Dashboard do organizador - metricas de vendas
 *
 * Returns:
 * - Total tickets sold
 * - Total revenue
 * - Total number of purchases
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetEventMetricsHandler {

    private final PurchaseRepository purchaseRepository;

    public EventMetricsDTO execute(UUID eventId) {
        log.info("Fetching metrics for event: {}", eventId);

        // Get total tickets sold (sum of quantities from confirmed purchases)
        Long totalTicketsSold = purchaseRepository.countTicketsByEventId(eventId);

        // Get total revenue (sum of total amounts from confirmed purchases)
        BigDecimal totalRevenue = purchaseRepository.sumTotalAmountByEventId(eventId);

        // Get total number of CONFIRMED purchases only
        Long totalPurchases = (long) purchaseRepository.findByEventId(eventId).stream()
                .filter(p -> "CONFIRMED".equals(p.getStatus().name()))
                .count();

        log.info("Event {} metrics: {} tickets sold, revenue: {}, {} purchases",
                eventId, totalTicketsSold, totalRevenue, totalPurchases);

        return EventMetricsDTO.builder()
                .eventId(eventId)
                .totalTicketsSold(totalTicketsSold != null ? totalTicketsSold : 0L)
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .totalPurchases(totalPurchases)
                .build();
    }
}
