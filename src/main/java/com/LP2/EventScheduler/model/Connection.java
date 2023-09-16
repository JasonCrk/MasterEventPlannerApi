package com.LP2.EventScheduler.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "connections")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "connector_id", nullable = false)
    private User connector;

    @ManyToOne
    @JoinColumn(name = "connecting_id", nullable = false)
    private User connecting;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime connectedAt;
}
