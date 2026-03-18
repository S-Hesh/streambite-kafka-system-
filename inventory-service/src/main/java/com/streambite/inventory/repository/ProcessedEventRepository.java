package com.streambite.inventory.repository;

import com.streambite.inventory.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {

    // existsById is provided by JpaRepository for free —
    // it checks if a row with this eventId exists in the table.
    // We use it before processing any event to detect duplicates.
}