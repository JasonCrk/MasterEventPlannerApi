package com.LP2.EventScheduler.service.connection;

import com.LP2.EventScheduler.dto.connection.SendInvitationDTO;
import com.LP2.EventScheduler.email.EmailService;
import com.LP2.EventScheduler.email.mails.InvitationAcceptedEmail;
import com.LP2.EventScheduler.email.mails.InvitationRejectedEmail;
import com.LP2.EventScheduler.email.mails.ReceivedInvitationEmail;
import com.LP2.EventScheduler.exception.*;
import com.LP2.EventScheduler.model.Connection;
import com.LP2.EventScheduler.model.Invitation;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.model.enums.InvitationStatus;
import com.LP2.EventScheduler.repository.ConnectionRepository;
import com.LP2.EventScheduler.repository.InvitationRepository;
import com.LP2.EventScheduler.repository.UserRepository;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.connection.ConnectionMapper;
import com.LP2.EventScheduler.response.connection.ConnectionResponse;
import com.LP2.EventScheduler.response.invitation.InvitationMapper;
import com.LP2.EventScheduler.response.invitation.InvitationResponse;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;

    private final EmailService emailService;

    @Override
    public ListResponse<ConnectionResponse> retrieveConnections(User authUser) {
        List<Connection> userConnections = this.connectionRepository.findByUser(authUser);
        return new ListResponse<>(ConnectionMapper.INSTANCE.toList(userConnections, authUser));
    }

    @Override
    public ListResponse<InvitationResponse> retrieveUserInvitations(User authUser) {
        List<Invitation> userInvitations = this.invitationRepository
                .findByInvitingAndStatusOrderByNotifiedAtDesc(authUser, InvitationStatus.PENDING);
        return new ListResponse<>(InvitationMapper.INSTANCE.toList(userInvitations));
    }

    @Transactional
    @Override
    public MessageResponse sendInvitation(SendInvitationDTO invitationData, User authUser) {
        User inviting = this.userRepository
                .findById(invitationData.getUser())
                .orElseThrow(UserNotFoundException::new);

        boolean invitationExists = this.invitationRepository.existsByInviterAndInviting(authUser, inviting);

        if (invitationExists)
            throw new ResourceExistException("You have already sent an invitation to the user");

        Invitation invitation = Invitation.builder()
                .inviter(authUser)
                .inviting(inviting)
                .build();

        this.invitationRepository.save(invitation);

        Map<String, String> emailData = new HashMap<>();
        emailData.put("inviterPicture", authUser.getAccount().getPicture());
        emailData.put("inviterUsername", authUser.getUserName());
        emailData.put("invitingPicture", inviting.getAccount().getPicture());
        emailData.put("invitingUsername", inviting.getUserName());

        CompletableFuture.runAsync(() -> this.emailService.sendEmail(
                inviting.getEmail(),
                new ReceivedInvitationEmail(),
                emailData
        ));

        return new MessageResponse("Invitation sent successfully");
    }

    @Transactional
    @Override
    public MessageResponse acceptInvitation(UUID invitationId, User authUser) {
        Invitation invitation = this.invitationRepository
                .findById(invitationId)
                .orElseThrow(InvitationNotFoundException::new);

        if (!invitation.getInviting().getId().equals(authUser.getId()))
            throw new UnexpectedResourceValueException("The invitation is not for you");

        if (!invitation.getStatus().equals(InvitationStatus.PENDING))
            throw new UnexpectedResourceValueException("The invitation must be a PENDING status");

        invitation.setStatus(InvitationStatus.ACCEPTED);
        this.invitationRepository.save(invitation);

        Connection connection = Connection.builder()
                .connector(invitation.getInviter())
                .connecting(authUser)
                .invitation(invitation)
                .build();

        this.connectionRepository.save(connection);

        Map<String, String> emailData = new HashMap<>();
        emailData.put("inviterPicture", invitation.getInviter().getAccount().getPicture());
        emailData.put("inviterUsername", invitation.getInviter().getUserName());
        emailData.put("invitingPicture", authUser.getAccount().getPicture());
        emailData.put("invitingUsername", authUser.getUserName());

        CompletableFuture.runAsync(() -> this.emailService.sendEmail(
                invitation.getInviter().getEmail(),
                new InvitationAcceptedEmail(),
                emailData
        ));

        return new MessageResponse("Invitation accepted");
    }

    @Override
    public MessageResponse rejectInvitation(UUID invitationId, User authUser) {
        Invitation invitation = this.invitationRepository
                .findById(invitationId)
                .orElseThrow(InvitationNotFoundException::new);

        if (!invitation.getStatus().equals(InvitationStatus.PENDING))
            throw new UnexpectedResourceValueException("The invitation must be status pending");

        if (!invitation.getInviting().getId().equals(authUser.getId()))
            throw new UnexpectedResourceValueException("The invitation is not for you");

        this.invitationRepository.delete(invitation);

        Map<String, String> emailData = new HashMap<>();
        emailData.put("inviterPicture", invitation.getInviter().getAccount().getPicture());
        emailData.put("inviterUsername", invitation.getInviter().getUserName());
        emailData.put("invitingPicture", authUser.getAccount().getPicture());
        emailData.put("invitingUsername", authUser.getUserName());

        CompletableFuture.runAsync(() -> this.emailService.sendEmail(
                invitation.getInviter().getEmail(),
                new InvitationRejectedEmail(),
                emailData
        ));

        return new MessageResponse("Rejected invitation");
    }

    @Override
    public MessageResponse removeConnection(UUID connectionId, User authUser) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new ConnectionNotFoundException("The connection does not exist"));

        if (!connection.getConnector().getId().equals(authUser.getId()) &&
                !connection.getConnecting().getId().equals(authUser.getId()))
            throw new IsNotOwnerException("You are not authorized to remove this connection");

        connectionRepository.delete(connection);

        return new MessageResponse("Connection removed successfully");
    }
}
