package com.LP2.EventScheduler.repository;

import com.LP2.EventScheduler.model.Event;
import com.LP2.EventScheduler.repository.custom.SearchEventsRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>, SearchEventsRepository {
}
