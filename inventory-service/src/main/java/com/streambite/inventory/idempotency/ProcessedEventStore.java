package com.streambite.inventory.idempotency;

import com.streambite.inventory.model.ProcessedEvent;
import com.streambite.inventory.repository.ProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ProcessedEventStore {

    private static final Logger log = LoggerFactory.getLogger(ProcessedEventStore.class);

    private final ProcessedEventRepository repo;

    public ProcessedEventStore(ProcessedEventRepository repo) {
        this.repo = repo;
    }

    /**
     * Checks DB — survives restarts unlike the old ConcurrentHashMap.
     */
    public boolean isAlreadyProcessed(String eventId) {
        return repo.existsById(eventId);
    }

    /**
     * Saves eventId to DB. If two threads race to insert the same eventId,
     * the second gets a DataIntegrityViolationException (PK violation),
     * which we catch and log safely — duplicate is simply skipped.
     */
    public void markProcessed(String eventId) {
        try {
            repo.save(new ProcessedEvent(eventId, Instant.now()));
        } catch (DataIntegrityViolationException e) {
            log.warn("Race condition on eventId {} — already marked by another thread",
                    eventId);
        }
    }
}