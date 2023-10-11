package com.LP2.EventScheduler.service.connection;

import com.LP2.EventScheduler.dto.connection.SendInvitationDTO;
import com.LP2.EventScheduler.email.EmailService;
import com.LP2.EventScheduler.email.mails.InvitationAcceptedEmail;
import com.LP2.EventScheduler.email.mails.ReceivedInvitationEmail;
import com.LP2.EventScheduler.exception.*;
import com.LP2.EventScheduler.model.Connection;
import com.LP2.EventScheduler.model.Invitation;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.model.enums.InvitationStatus;
import com.LP2.EventScheduler.repository.ConnectionRepository;
import com.LP2.EventScheduler.repository.InvitationRepository;
import com.LP2.EventScheduler.repository.UserRepository;
import com.LP2.EventScheduler.response.MessageResponse;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;

    private final EmailService emailService;

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

        try {
            this.emailService.sendEmail(inviting.getEmail(), new ReceivedInvitationEmail(), emailData);
        } catch (MessagingException e) {
            throw new FailedEmailSendingException();
        }

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
                .build();

        this.connectionRepository.save(connection);

        Map<String, String> emailData = new HashMap<>();
        emailData.put("inviterPicture", invitation.getInviter().getAccount().getPicture());
        emailData.put("inviterUsername", invitation.getInviter().getUserName());
        emailData.put("invitingPicture", authUser.getAccount().getPicture());
        emailData.put("invitingUsername", authUser.getUserName());

        try {
            this.emailService.sendEmail(
                    invitation.getInviter().getEmail(),
                    new InvitationAcceptedEmail(),
                    emailData
            );
        } catch (MessagingException e) {
            throw new FailedEmailSendingException();
        }

        return new MessageResponse("Invitation accepted");
    }
}
