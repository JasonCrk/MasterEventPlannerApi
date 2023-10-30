package com.LP2.EventScheduler.repository;

import com.LP2.EventScheduler.model.Invitation;
import com.LP2.EventScheduler.model.User;

import com.LP2.EventScheduler.model.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {
    List<Invitation> findByInvitingAndStatusOrderByNotifiedAtDesc(User inviting, InvitationStatus status);
    Optional<Invitation> findByInviterAndInviting(User inviter, User inviting);
    boolean existsByInviterAndInviting(User inviter, User inviting);
}
