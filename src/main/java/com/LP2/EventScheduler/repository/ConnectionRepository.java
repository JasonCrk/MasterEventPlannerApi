package com.LP2.EventScheduler.repository;

import com.LP2.EventScheduler.model.Connection;
import com.LP2.EventScheduler.model.keys.ConnectionId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, ConnectionId> {
}
