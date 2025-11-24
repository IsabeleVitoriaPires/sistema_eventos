package com.eventservice.infrastructure.event.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA Repository for EventEntity
 */
@Repository
public interface JpaEventRepository extends JpaRepository<EventEntity, UUID> {

    List<EventEntity> findByNameContainingIgnoreCase(String name);

    List<EventEntity> findByOrganizerId(UUID organizerId);
}
