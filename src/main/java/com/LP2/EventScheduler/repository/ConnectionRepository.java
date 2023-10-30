package com.LP2.EventScheduler.repository;

import com.LP2.EventScheduler.model.Connection;
import com.LP2.EventScheduler.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
    @Query(value = """
           SELECT COUNT(c) > 0 FROM Connection c WHERE\s
           (c.connector = :connector AND c.connecting = :connecting) OR\s
           (c.connecting = :connecting AND c.connector = :connector)
           """)
    boolean existsConnectionBetweenUsers(User connector, User connecting);

    @Query(value = """
            SELECT c FROM Connection c WHERE\s
            c.connector = :user OR c.connecting = :user
            ORDER BY c.connectedAt DESC
            """)
    List<Connection> findByUser(@Param("user") User user);
}
