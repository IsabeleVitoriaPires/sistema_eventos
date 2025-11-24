package com.eventservice.infrastructure.event.persistence;

import com.eventservice.domain.event.Event;
import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter Pattern: Adapts JPA repository to domain repository interface
 * Infrastructure layer implementation of EventRepository port
 */
@Component
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepository {

    private final JpaEventRepository jpaEventRepository;

    @Override
    public Event save(Event event) {
        EventEntity entity = EventEntity.fromDomain(event);
        EventEntity savedEntity = jpaEventRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Event> findById(UUID id) {
        return jpaEventRepository.findById(id)
                .map(EventEntity::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return jpaEventRepository.findAll()
                .stream()
                .map(EventEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByNameContainingIgnoreCase(String name) {
        return jpaEventRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(EventEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByOrganizerId(UUID organizerId) {
        return jpaEventRepository.findByOrganizerId(organizerId)
                .stream()
                .map(EventEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaEventRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaEventRepository.existsById(id);
    }
}
