package com.LP2.EventScheduler.model.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@Embeddable
public class InvitationId implements Serializable {
    @Column(name = "inviter_id")
    UUID inviterId;

    @Column(name = "inviting_id")
    UUID invitingId;
}
