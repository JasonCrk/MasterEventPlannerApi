package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.dto.connection.SendInvitationDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.ListResponse;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.connection.ConnectionResponse;
import com.LP2.EventScheduler.response.invitation.InvitationResponse;
import com.LP2.EventScheduler.service.connection.ConnectionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Connection")
@RestController
@RequestMapping(path = "/api/connections")
@RequiredArgsConstructor
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @Operation(summary = "Retrieve the user connections")
    @GetMapping
    public ResponseEntity<ListResponse<ConnectionResponse>> retrieveUserConnections(
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.connectionService.retrieveConnections(authUser));
    }

    @Operation(summary = "Retrieve the user invitations (notifications)")
    @GetMapping(path = "/invitations")
    public ResponseEntity<ListResponse<InvitationResponse>> retrieveUserInvitations(
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.connectionService.retrieveUserInvitations(authUser));
    }

    @Operation(summary = "Send an invitation to a user to establish a connection")
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

    @Operation(summary = "Accept a user's invitation")
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

    @Operation(summary = "Reject a user's invitation")
    @DeleteMapping("/reject-invitation/{invitationId}")
    public ResponseEntity<MessageResponse> rejectInvitation(
            @PathVariable("invitationId") UUID invitationId,
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.connectionService.rejectInvitation(invitationId, authUser));
    }

    @Operation(summary = "Delete a connection")
    @DeleteMapping("/{connectionId}")
    public ResponseEntity<MessageResponse> removeConnection(
            @PathVariable("connectionId") UUID connectionId,
            @RequestAttribute("user") User authUser) {
        return ResponseEntity.ok(connectionService.removeConnection(connectionId, authUser));
    }
}
