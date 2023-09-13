package com.LP2.EventScheduler.controller;

import com.LP2.EventScheduler.dto.LoginDTO;
import com.LP2.EventScheduler.dto.RegisterDTO;
import com.LP2.EventScheduler.responses.auth.JwtResponse;
import com.LP2.EventScheduler.service.auth.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginDTO loginData
    ) {
        return ResponseEntity.ok(this.authService.login(loginData));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<JwtResponse> register(
            @Valid @RequestBody RegisterDTO registerData
    ) {
        return new ResponseEntity<>(this.authService.register(registerData), HttpStatus.CREATED);
    }

    @PostMapping(path = "/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        this.authService.refreshToken(request, response);
    }
}
