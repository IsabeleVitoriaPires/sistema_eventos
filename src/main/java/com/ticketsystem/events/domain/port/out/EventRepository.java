package com.ticketsystem.events.domain.port.out;

import com.ticketsystem.events.domain.model.Event;

import java.util.List;
import java.util.Optional;

/**
 * Porta de saída para persistência de eventos
 */
public interface EventRepository {

    Event save(Event event);

    Optional<Event> findById(Long id);

    List<Event> findAll();

    List<Event> findByNameContainingIgnoreCase(String name);

    List<Event> findByOrganizerId(Long organizerId);

    void deleteById(Long id);

    boolean existsById(Long id);
}
