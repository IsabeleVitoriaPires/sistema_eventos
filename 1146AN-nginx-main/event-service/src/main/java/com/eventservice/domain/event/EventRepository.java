package com.eventservice.domain.event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository Port (Interface)
 * Defines the contract for event persistence
 * Implementation will be in the infrastructure layer
 */
public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(UUID id);
    List<Event> findAll();
    List<Event> findByNameContainingIgnoreCase(String name);
    List<Event> findByOrganizerId(UUID organizerId);
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
