package com.LP2.EventScheduler.repository;

import com.LP2.EventScheduler.model.Invitation;
import com.LP2.EventScheduler.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    boolean existsByInviterAndInviting(User inviter, User inviting);
}
