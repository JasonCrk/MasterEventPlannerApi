package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.dto.auth.LoginDTO;
import com.LP2.EventScheduler.dto.auth.RegisterDTO;
import com.LP2.EventScheduler.model.User;
import com.LP2.EventScheduler.response.MessageResponse;
import com.LP2.EventScheduler.response.auth.JwtResponse;
import com.LP2.EventScheduler.response.user.SimpleUserResponse;
import com.LP2.EventScheduler.service.auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth")
@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Retrieve user by access token")
    @GetMapping(path = "/user")
    public ResponseEntity<SimpleUserResponse> retrieveUserByToken(
            @RequestAttribute("user") User authUser
    ) {
        return ResponseEntity.ok(this.authService.retrieveUserByToken(authUser));
    }

    @Operation(summary = "Verify access token")
    @GetMapping(path = "/verify")
    public ResponseEntity<MessageResponse> verifyToken(
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(this.authService.verifyToken(request));
    }

    @Operation(summary = "Login")
    @PostMapping(path = "/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginDTO loginData
    ) {
        return new ResponseEntity<>(this.authService.login(loginData), HttpStatus.OK);
    }

    @Operation(summary = "Sign up")
    @PostMapping(path = "/register")
    public ResponseEntity<JwtResponse> register(
            @Valid @RequestBody RegisterDTO registerData
    ) {
        return new ResponseEntity<>(this.authService.register(registerData), HttpStatus.CREATED);
    }

    @Operation(summary = "Refresh access token")
    @PostMapping(path = "/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return new ResponseEntity<>(
                this.authService.refreshToken(request, response),
                HttpStatus.OK
        );
    }
}
