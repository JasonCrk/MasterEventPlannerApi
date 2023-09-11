package com.LP2.EventScheduler.repository;

import com.LP2.EventScheduler.model.Invitation;
import com.LP2.EventScheduler.model.keys.InvitationId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, InvitationId> {
}
