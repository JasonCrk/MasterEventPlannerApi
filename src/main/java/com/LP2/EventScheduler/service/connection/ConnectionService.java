package com.LP2.EventScheduler.service.connection;

import com.LP2.EventScheduler.dto.connection.SendInvitationDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.connection.ConnectionResponse;

import java.util.UUID;

public interface ConnectionService {
    ListResponse<ConnectionResponse> retrieveConnections(User authUser);
    MessageResponse sendInvitation(SendInvitationDTO invitationData, User authUser);
    MessageResponse acceptInvitation(UUID invitationId, User user);
}
