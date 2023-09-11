package com.LP2.EventScheduler.model;

import com.LP2.EventScheduler.model.keys.ConnectionId;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "connections")
public class Connection {
    @EmbeddedId
    private ConnectionId id;

    @ManyToOne
    @MapsId("connector")
    @JoinColumn(name = "connector_id")
    private User connector;

    @ManyToOne
    @MapsId("connectingId")
    @JoinColumn(name = "connecting_id")
    private User connecting;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime connectedAt;
}
