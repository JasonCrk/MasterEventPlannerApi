package com.LP2.EventScheduler.model.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@Embeddable
public class ConnectionId implements Serializable {
    @Column(name = "connector_id")
    UUID connectorId;

    @Column(name = "connecting_id")
    UUID connectingId;
}
