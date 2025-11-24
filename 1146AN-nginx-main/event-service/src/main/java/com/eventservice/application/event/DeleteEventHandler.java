package com.eventservice.application.event;

import com.eventservice.domain.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Use Case: Delete an event
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteEventHandler {

    private final EventRepository eventRepository;

    /**
     * Execute: Delete event by ID
     * @param eventId Event UUID
     * @throws EventNotFoundException if event not found
     */
    @Transactional
    public void execute(UUID eventId) {
        log.info("Deleting event with ID: {}", eventId);

        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Event not found with id: " + eventId);
        }

        eventRepository.deleteById(eventId);

        log.info("Event deleted successfully: {}", eventId);
    }
}
