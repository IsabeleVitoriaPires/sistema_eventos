package com.ticketsystem.infrastructure.client;

import com.ticketsystem.application.purchase.EventDTO;
import com.ticketsystem.application.purchase.EventNotFoundException;
import com.ticketsystem.application.purchase.EventServiceClient;
import com.ticketsystem.application.purchase.InsufficientTicketsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * HTTP Client for Event Service communication
 * Uses WebClient for non-blocking REST calls
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventServiceClientImpl implements EventServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${event-service.url:http://localhost:8083}")
    private String eventServiceUrl;

    @Override
    public EventDTO getEvent(UUID eventId) {
        log.info("Fetching event {} from event-service", eventId);

        try {
            WebClient webClient = webClientBuilder.baseUrl(eventServiceUrl).build();

            EventDTO event = webClient.get()
                    .uri("/api/events/{id}", eventId)
                    .retrieve()
                    .onStatus(status -> status == HttpStatus.NOT_FOUND,
                            response -> Mono.error(new EventNotFoundException("Event not found: " + eventId)))
                    .bodyToMono(EventDTO.class)
                    .block();

            log.info("Event {} fetched successfully: {}", eventId, event.getName());
            return event;

        } catch (WebClientResponseException.NotFound e) {
            log.error("Event {} not found", eventId);
            throw new EventNotFoundException("Event not found with id: " + eventId);
        } catch (Exception e) {
            log.error("Error fetching event {}: {}", eventId, e.getMessage());
            throw new RuntimeException("Error communicating with event-service: " + e.getMessage(), e);
        }
    }

    @Override
    public void reserveTickets(UUID eventId, int quantity) {
        log.info("Reserving {} tickets for event {}", quantity, eventId);

        try {
            WebClient webClient = webClientBuilder.baseUrl(eventServiceUrl).build();

            // Create request body
            ReserveTicketsRequest request = new ReserveTicketsRequest(quantity);

            webClient.patch()
                    .uri("/api/events/{id}/reserve", eventId)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(status -> status == HttpStatus.NOT_FOUND,
                            response -> Mono.error(new EventNotFoundException("Event not found: " + eventId)))
                    .onStatus(status -> status == HttpStatus.BAD_REQUEST,
                            response -> Mono.error(new InsufficientTicketsException(
                                    "Insufficient tickets for event " + eventId)))
                    .bodyToMono(Void.class)
                    .block();

            log.info("Tickets reserved successfully for event {}", eventId);

        } catch (WebClientResponseException.NotFound e) {
            log.error("Event {} not found", eventId);
            throw new EventNotFoundException("Event not found with id: " + eventId);
        } catch (WebClientResponseException.BadRequest e) {
            log.error("Insufficient tickets for event {}", eventId);
            throw new InsufficientTicketsException("Insufficient tickets for event " + eventId);
        } catch (Exception e) {
            log.error("Error reserving tickets for event {}: {}", eventId, e.getMessage());
            throw new RuntimeException("Error communicating with event-service: " + e.getMessage(), e);
        }
    }

    /**
     * Internal DTO for reserve tickets request
     */
    private static class ReserveTicketsRequest {
        private final int quantity;

        public ReserveTicketsRequest(int quantity) {
            this.quantity = quantity;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
