package com.LP2.EventScheduler.model;

import com.LP2.EventScheduler.model.enums.InvitationStatus;
import com.LP2.EventScheduler.model.keys.InvitationId;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "invitations")
public class Invitation {
    @EmbeddedId
    private InvitationId id;

    @ManyToOne
    @MapsId("inviterId")
    @JoinColumn(name = "inviter_id")
    private User inviter;

    @ManyToOne
    @MapsId("invitingId")
    @JoinColumn(name = "inviting_id")
    private User inviting;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    private InvitationStatus status = InvitationStatus.PENDING;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime notifiedAt = LocalDateTime.now();
}
