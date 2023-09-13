package com.LP2.EventScheduler.model;

import com.LP2.EventScheduler.model.enums.EventStatus;
import com.LP2.EventScheduler.model.enums.Visibility;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "coordinator_id", nullable = false)
    private User coordinator;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(nullable = false)
    private String local;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime realizationDate;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "eventsParticipating")
    private List<User> participants;
}
