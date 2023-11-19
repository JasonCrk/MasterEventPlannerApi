package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.dto.connection.SendInvitationDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.connection.ConnectionResponse;
import com.LP2.EventScheduler.response.invitation.InvitationResponse;
import com.LP2.EventScheduler.service.connection.ConnectionService;


import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/connections")
@RequiredArgsConstructor
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @GetMapping
    public ResponseEntity<ListResponse<ConnectionResponse>> retrieveUserConnections(
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.connectionService.retrieveConnections(authUser));
    }

    @GetMapping(path = "/invitations")
    public ResponseEntity<ListResponse<InvitationResponse>> retrieveUserInvitations(
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.connectionService.retrieveUserInvitations(authUser));
    }

    @PostMapping(path = "/send-invitation")
    public ResponseEntity<MessageResponse> sendInvitation(
            @RequestAttribute("user") User user,
            @Valid @RequestBody SendInvitationDTO invitationData
    ) {
        return new ResponseEntity<>(
                this.connectionService.sendInvitation(invitationData, user),
                HttpStatus.CREATED
        );
    }

    @PostMapping(path = "/accept-invitation/{invitationId}")
    public ResponseEntity<MessageResponse> acceptInvitation(
            @PathVariable("invitationId") UUID invitationId,
            @RequestAttribute("user") User authUser
    ) {
        return new ResponseEntity<>(
                this.connectionService.acceptInvitation(invitationId, authUser),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/reject-invitation/{invitationId}")
    public ResponseEntity<MessageResponse> rejectInvitation(
            @PathVariable("invitationId") UUID invitationId,
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.connectionService.rejectInvitation(invitationId, authUser));
    }

    @DeleteMapping("/{connectionId}")
    public ResponseEntity<MessageResponse> removeConnection(
            @PathVariable("connectionId") UUID connectionId,
            @RequestAttribute("user") User authUser) {
        return ResponseEntity.ok(connectionService.removeConnection(connectionId,authUser));
    }
}
