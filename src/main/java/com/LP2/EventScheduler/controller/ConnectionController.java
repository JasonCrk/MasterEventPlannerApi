package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.dto.connection.SendInvitationDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.service.connection.ConnectionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/connections")
@RequiredArgsConstructor
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

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
}
