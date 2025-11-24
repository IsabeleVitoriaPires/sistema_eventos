package com.eventservice.infrastructure.client;

import com.eventservice.application.event.EventMetricsDTO;
import com.eventservice.application.event.MetricsServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * HTTP Client for Metrics Service (ticket-service) communication
 * Uses WebClient for non-blocking REST calls
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsServiceClientImpl implements MetricsServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${ticket-service.url:http://localhost:8085}")
    private String ticketServiceUrl;

    @Override
    public EventMetricsDTO getEventMetrics(UUID eventId) {
        log.info("Fetching metrics for event {} from ticket-service", eventId);

        try {
            WebClient webClient = webClientBuilder.baseUrl(ticketServiceUrl).build();

            EventMetricsDTO metrics = webClient.get()
                    .uri("/api/metrics/events/{eventId}", eventId)
                    .retrieve()
                    .bodyToMono(EventMetricsDTO.class)
                    .block();

            log.info("Metrics fetched for event {}: {} tickets sold, revenue: {}",
                    eventId, metrics.getTotalTicketsSold(), metrics.getTotalRevenue());

            return metrics;

        } catch (Exception e) {
            log.error("Error fetching metrics for event {}: {}", eventId, e.getMessage());
            // Return empty metrics if service is unavailable
            return EventMetricsDTO.builder()
                    .eventId(eventId)
                    .totalTicketsSold(0L)
                    .totalRevenue(BigDecimal.ZERO)
                    .totalPurchases(0L)
                    .build();
        }
    }
}
