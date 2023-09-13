package com.LP2.EventScheduler.repository;

import com.LP2.EventScheduler.model.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    @Query(value = """
            SELECT t FROM Token t INNER JOIN User u\s
            ON t.user.id = u.id\s
            WHERE u.id = :id and (t.expired = false and t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(UUID id);

    Optional<Token> findByToken(String token);
}
